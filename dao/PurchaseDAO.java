package dao;

import gds.Purchase;
import java.util.List;

public interface PurchaseDAO {
    // 1. 새로운 구매(주문서) 내역 저장
    void save(Purchase purchase);

    // 2. 주문 번호(ID)로 특정 주문 내역 조회
    Purchase findById(String purchaseId);

    // 3. 특정 유저의 전체 구매 내역(영수증 목록) 조회
    List<Purchase> findByUserId(String userId);

    // 4. 주문 상태 업데이트 (예: PENDING(대기) -> COMPLETED(완료) 또는 REFUNDED(환불))
    void updateStatus(String purchaseId, String status); 
}