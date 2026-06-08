package service;

import org.springframework.stereotype.Service;

@Service
public class TossPaymentGatewayImpl implements TossPaymentGateway {

    @Override
    public boolean confirmPayment(String paymentKey, String orderId, int amount) {
        // 실제 운영 환경에서는 이곳에서 Toss Payments API로 HTTP POST 요청을 보냅니다.
        // 현재는 서버 콘솔 출력으로 성공을 모킹(Mocking)합니다.
        System.out.println("Toss API 통신 (Mocking)...");
        System.out.println("PaymentKey: " + paymentKey);
        System.out.println("OrderId: " + orderId);
        System.out.println("Amount: " + amount);
        System.out.println("토스 결제 승인 완료!");
        
        return true;
    }
}