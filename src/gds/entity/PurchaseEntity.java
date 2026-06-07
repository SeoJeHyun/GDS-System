package entity;

public class PurchaseEntity {
    private String purchaseId;
    private String userId; // 외래키
    private String purchaseDate;
    private String purchaseStatus;
    private int totalAmount;

    public PurchaseEntity(String purchaseId, String userId, String purchaseDate, String purchaseStatus, int totalAmount) {
        this.purchaseId = purchaseId;
        this.userId = userId;
        this.purchaseDate = purchaseDate;
        this.purchaseStatus = purchaseStatus;
        this.totalAmount = totalAmount;
    }

    public String getPurchaseId() { return purchaseId; }
    public String getUserId() { return userId; }
    public String getPurchaseDate() { return purchaseDate; }
    public String getPurchaseStatus() { return purchaseStatus; }
    public int getTotalAmount() { return totalAmount; }
}