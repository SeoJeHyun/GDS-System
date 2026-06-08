package service;

import org.springframework.stereotype.Service;


@Service
public class TossPaymentGatewayImpl implements TossPaymentGateway {

    @Override
    public boolean confirmPayment(String paymentKey, String orderId, int amount) {
        // 💡 프론트엔드에서 실제 토스 위젯을 연동하기 전까지는, 가짜 키가 넘어오므로 실제 서버 통신을 생략(모킹)합니다.
        System.out.println("🚀 토스 결제 승인 모킹(Mocking) 작동 중...");
        System.out.println("요청 데이터 - PaymentKey: " + paymentKey + " | OrderId: " + orderId + " | Amount: " + amount);
        System.out.println("✅ 토스 결제 승인 성공 처리 완료!");
        
        return true; // 무조건 결제 성공으로 처리하여 다음 로직(라이브러리 추가)으로 넘어가게 함
    }
}