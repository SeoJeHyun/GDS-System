package dao;

import gds.Payment;
import java.util.List;

public interface PaymentDAO {
    // 1. 실제 결제 트랜잭션 기록 저장
    void save(Payment payment);

    // 2. 특정 주문(Purchase)에 엮여 있는 결제 기록들 조회 (복합 결제 고려)
    List<Payment> findByPurchaseId(String purchaseId);
    
    // 3. 특정 결제 건의 승인 상태 업데이트 (예: 승인됨, 잔액 부족 거절 등)
    void updateStatus(String paymentId, String status);
}
