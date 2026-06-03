package gds;

import dao.PaymentDAO;
import dto.PaymentResponseDTO;
import entity.PaymentEntity;

public class Payment {
    private final String paymentId;
    private final String purchaseId; 
    private final String paymentMethodId; 
    private final int amount;
    private String status;

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

    public PaymentResponseDTO toDTO() {
        return new PaymentResponseDTO(paymentId, paymentMethodId, amount, status);
    }

    public static Payment toDomain(PaymentEntity entity, PaymentDAO dao) {
        return new Payment(entity.getPaymentId(), entity.getPurchaseId(), 
                           entity.getPaymentMethodId(), entity.getAmount(), entity.getPaymentStatus(), dao);
    }
}