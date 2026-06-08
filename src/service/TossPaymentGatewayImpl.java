package service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class TossPaymentGatewayImpl implements TossPaymentGateway {

    @Override
    public boolean confirmPayment(String paymentKey, String orderId, int amount) {
        System.out.println("🚀 실제 Toss Payments API 승인 요청 시작...");
        System.out.println("PaymentKey: " + paymentKey + " | OrderId: " + orderId + " | Amount: " + amount);

        try {
            RestTemplate restTemplate = new RestTemplate();
            String tossConfirmUrl = "https://api.tosspayments.com/v1/payments/confirm";
            
            HttpHeaders headers = new HttpHeaders();
            // 토스페이먼츠 테스트용 시크릿 키 (개발 환경용)
            String secretKey = "test_sk_zXLkKEypNArWmo50nX3lmeaxYG5R"; 
            String encodedAuth = Base64.getEncoder().encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8));
            
            headers.set("Authorization", "Basic " + encodedAuth);
            headers.set("Content-Type", "application/json");

            Map<String, Object> params = new HashMap<>();
            params.put("paymentKey", paymentKey);
            params.put("orderId", orderId);
            params.put("amount", amount);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(params, headers);
            
            // 🌐 실제 HTTP 통신 발생! (토스 서버 찌르기)
            ResponseEntity<Map> response = restTemplate.exchange(tossConfirmUrl, HttpMethod.POST, requestEntity, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("✅ 토스 결제 승인 완료! 응답 데이터: " + response.getBody());
                return true;
            } else {
                System.out.println("❌ 토스 결제 승인 실패 (HTTP 상태 코드: " + response.getStatusCode() + ")");
                return false;
            }
        } catch (Exception e) {
            System.out.println("🚨 토스 결제 승인 중 예외 발생: " + e.getMessage());
            return false;
        }
    }
}