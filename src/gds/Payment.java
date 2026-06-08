package gds;

import dao.PaymentDAO;
import dto.PaymentResponseDTO;
import entity.PaymentEntity;
import service.TossPaymentGateway;

public class Payment {
    private final String paymentId;
    private final String purchaseId; 
    private final String paymentMethodId; 
    private final int amount;
    private String status;
    private String paymentKey; // 토스 결제키 추가

    private final PaymentDAO paymentDAO;

    public Payment(String paymentId, String purchaseId, String paymentMethodId, 
                   int amount, String status, PaymentDAO paymentDAO) {
        this.paymentId = paymentId;
        this.purchaseId = purchaseId;
        this.paymentMethodId = paymentMethodId;
        this.amount = amount;
        this.status = status;
        this.paymentDAO = paymentDAO;
    }

    public void approve() {
        this.status = "PAID";
        this.paymentDAO.updateStatus(this.paymentId, this.status);
    }
    
    // 객체 스스로 토스 게이트웨이를 이용해 결제 승인을 받고 상태를 업데이트하는 로직
    public boolean processToss(TossPaymentGateway gateway, String paymentKey, String orderId) {
        this.paymentKey = paymentKey;
        boolean isSuccess = gateway.confirmPayment(paymentKey, orderId, this.amount);
        
        if (isSuccess) {
            approve();
            return true;
        }
        return false;
    }

    public PaymentResponseDTO toDTO() {
        return new PaymentResponseDTO(paymentId, paymentMethodId, amount, status);
    }

    public static Payment toDomain(PaymentEntity entity, PaymentDAO dao) {
        return new Payment(entity.getPaymentId(), entity.getPurchaseId(), 
                           entity.getPaymentMethodId(), entity.getAmount(), entity.getPaymentStatus(), dao);
    }

    // --- DAO가 DB 저장을 위해 값을 읽어갈 수 있도록 Getter 추가 ---
    public String getPaymentId() { return paymentId; }
    public String getPurchaseId() { return purchaseId; }
    public String getPaymentMethodId() { return paymentMethodId; }
    public int getAmount() { return amount; }
    public String getStatus() { return status; }
    public String getPaymentKey() { return paymentKey; }
}