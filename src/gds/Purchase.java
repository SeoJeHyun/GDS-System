package gds;

import dao.PurchaseDAO;
import dto.PurchaseResponseDTO;
import entity.PurchaseEntity;

public class Purchase {
    private final String purchaseId;
    private final String userId; 
    private final int totalAmount;
    private String status; 
    
    private final PurchaseDAO purchaseDAO;

    public Purchase(String purchaseId, String userId, int totalAmount, String status, PurchaseDAO purchaseDAO) {
        this.purchaseId = purchaseId;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.purchaseDAO = purchaseDAO;
    }

    public void complete() {
        this.status = "PAID";
        this.purchaseDAO.updateStatus(this.purchaseId, this.status);
    }

    public void fail() {
        this.status = "FAILED";
        this.purchaseDAO.updateStatus(this.purchaseId, this.status);
    }
    
    public PurchaseResponseDTO toDTO(String purchaseDateStr) {
        return new PurchaseResponseDTO(purchaseId, totalAmount, purchaseDateStr, status);
    }

    public static Purchase toDomain(PurchaseEntity entity, PurchaseDAO dao) {
        return new Purchase(entity.getPurchaseId(), entity.getUserId(), entity.getTotalAmount(), entity.getPurchaseStatus(), dao);
    }

    // --- DAO가 DB 저장을 위해 값을 읽어갈 수 있도록 Getter 추가 ---
    public String getPurchaseId() { return purchaseId; }
    public String getUserId() { return userId; }
    public int getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
}