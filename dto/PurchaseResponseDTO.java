package dto;

public class PurchaseResponseDTO {
    private String purchaseId;
    private int totalPrice;
    private String purchaseDate; // 콘솔 출력용 문자열 날짜
    private String status;       // PENDING, COMPLETED, REFUNDED 등

    public PurchaseResponseDTO(String purchaseId, int totalPrice, String purchaseDate, String status) {
        this.purchaseId = purchaseId;
        this.totalPrice = totalPrice;
        this.purchaseDate = purchaseDate;
        this.status = status;
    }

    public String getPurchaseId() { return purchaseId; }
    public int getTotalPrice() { return totalPrice; }
    public String getPurchaseDate() { return purchaseDate; }
    public String getStatus() { return status; }

    @Override
    public String toString() {
        return String.format("주문번호: %s | 결제일: %s | 총금액: %d원 | 상태: %s", 
                             purchaseId, purchaseDate, totalPrice, status);
    }
}
