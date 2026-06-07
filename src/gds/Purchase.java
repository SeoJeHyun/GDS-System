package gds;

import dao.PurchaseDAO;
import dto.PurchaseResponseDTO;
import entity.PurchaseEntity;

import java.util.ArrayList;
import java.util.List;

public class Purchase {
    // 결제 상태 상수 정의 (명확한 상태 통제)
    public static final String STATUS_PENDING = "PENDING";     // 결제 대기
    public static final String STATUS_COMPLETED = "COMPLETED"; // 결제 완료
    public static final String STATUS_FAILED = "FAILED";       // 결제 실패

    private final String purchaseId;
    private final String userId; 
    private final int totalAmount;
    private String status; 
    
    // 결제 당시의 게임 목록 스냅샷 (API 4 응답 및 위변조 방지용)
    private final List<Game> purchasedGames;
    
    private final PurchaseDAO purchaseDAO;

    public Purchase(String purchaseId, String userId, int totalAmount, String status, List<Game> purchasedGames, PurchaseDAO purchaseDAO) {
        this.purchaseId = purchaseId;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.purchasedGames = purchasedGames != null ? new ArrayList<>(purchasedGames) : new ArrayList<>();
        this.purchaseDAO = purchaseDAO;
    }

    // 장바구니를 기반으로 새로운 주문을 생성하는 팩토리 메서드 (스냅샷 동결)
    public static Purchase createFromCart(String purchaseId, String userId, Cart cart, PurchaseDAO purchaseDAO) {
        return new Purchase(purchaseId, userId, cart.getTotalAmount(), STATUS_PENDING, cart.getGames(), purchaseDAO);
    }

    // [풍부한 도메인 모델] 위변조 교차 검증 로직
    public void verifyAmount(int pgAmount) {
        if (this.totalAmount != pgAmount) {
            fail(); // 금액이 다르면 즉시 스스로 실패 처리
            throw new IllegalStateException("결제 금액이 일치하지 않습니다. 위변조가 의심됩니다.");
        }
    }

    // [풍부한 도메인 모델] 상태 전이 통제 (대기 중일 때만 완료 가능)
    public void complete() {
        if (!STATUS_PENDING.equals(this.status)) {
            throw new IllegalStateException("결제 대기 상태에서만 결제를 완료할 수 있습니다.");
        }
        this.status = STATUS_COMPLETED;
        this.purchaseDAO.updateStatus(this.purchaseId, this.status);
    }

    // [풍부한 도메인 모델] 상태 전이 통제 (대기 중일 때만 실패 가능)
    public void fail() {
        if (!STATUS_PENDING.equals(this.status)) {
            throw new IllegalStateException("결제 대기 상태에서만 실패 처리를 할 수 있습니다.");
        }
        this.status = "FAILED";
        this.purchaseDAO.updateStatus(this.purchaseId, this.status);
    }
    
    public PurchaseResponseDTO toDTO(String purchaseDateStr) {
        return new PurchaseResponseDTO(purchaseId, totalAmount, purchaseDateStr, status);
    }

    public static Purchase toDomain(PurchaseEntity entity, PurchaseDAO dao) {
        // DB에서 불러올 때는 스냅샷 리스트를 추후에 채우거나, 현재는 빈 상태로 둡니다.
        return new Purchase(entity.getPurchaseId(), entity.getUserId(), entity.getTotalAmount(), entity.getPurchaseStatus(), new ArrayList<>(), dao);
    }

    // --- DAO가 DB 저장을 위해 값을 읽어갈 수 있도록 Getter 추가 ---
    public String getPurchaseId() { return purchaseId; }
    public String getUserId() { return userId; }
    public int getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
}