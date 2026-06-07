package dao;

import gds.Purchase;
import java.util.List;

public interface PurchaseDAO {
    // 1. 새로운 주문 및 영수증 내역 DB에 저장
    void save(Purchase purchase);
    
    // 2. 주문 상태 업데이트 (대기 -> 완료 / 실패 등)
    void updateStatus(String purchaseId, String status);
    
    // 3. 특정 주문 내역 조회 (필요 시)
    Purchase findById(String purchaseId);
}