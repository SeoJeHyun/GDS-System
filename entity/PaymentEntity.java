package entity;

public class PaymentEntity {
    private String paymentId;
    private String purchaseId; // 어떤 주문에 대한 결제인지
    private String paymentMethodId;
    private int amount;
    private String paymentStatus;

    public PaymentEntity(String paymentId, String purchaseId, String paymentMethodId, int amount, String paymentStatus) {
        this.paymentId = paymentId;
        this.purchaseId = purchaseId;
        this.paymentMethodId = paymentMethodId;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentId() { return paymentId; }
    public String getPurchaseId() { return purchaseId; }
    public String getPaymentMethodId() { return paymentMethodId; }
    public int getAmount() { return amount; }
    public String getPaymentStatus() { return paymentStatus; }
}