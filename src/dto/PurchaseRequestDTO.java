package dto;

public class PurchaseRequestDTO {
    private String paymentKey; // PG사 결제 고유 키
    private String orderId;    // 우리가 생성했던 주문 번호
    private int amount;        // 프론트엔드가 실제로 결제했다고 주장하는 금액

    public PurchaseRequestDTO() {}

    public PurchaseRequestDTO(String paymentKey, String orderId, int amount) {
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.amount = amount;
    }

    public String getPaymentKey() { return paymentKey; }
    public String getOrderId() { return orderId; }
    public int getAmount() { return amount; }
}