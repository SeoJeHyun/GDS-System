package dto;

public class PaymentResponseDTO {
    private String paymentId;
    private String paymentMethod; // 예: "CARD", "POINT"
    private int amount;
    private String status;

    public PaymentResponseDTO(String paymentId, String paymentMethod, int amount, String status) {
        this.paymentId = paymentId;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.status = status;
    }

    public String getPaymentId() { return paymentId; }
    public String getPaymentMethod() { return paymentMethod; }
    public int getAmount() { return amount; }
    public String getStatus() { return status; }

    @Override
    public String toString() {
        return String.format("   [상세결제] 승인번호: %s | 결제수단: %s | 결제금액: %d원 (%s)", 
                             paymentId, paymentMethod, amount, status);
    }
}
