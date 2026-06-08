package service;

import dao.CartDAO;
import dao.LibraryDAO;
import dao.PurchaseDAO;
import dao.UserDAO;
import dto.PurchaseRequestDTO;
import gds.Game;
import gds.MemberGamer;
import gds.Purchase;
import gds.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PurchaseService {
    private final UserDAO userDAO;
    private final CartDAO cartDAO;
    private final LibraryDAO libraryDAO;
    private final PurchaseDAO purchaseDAO;

    public PurchaseService(UserDAO userDAO, CartDAO cartDAO, LibraryDAO libraryDAO, PurchaseDAO purchaseDAO) {
        this.userDAO = userDAO;
        this.cartDAO = cartDAO;
        this.libraryDAO = libraryDAO;
        this.purchaseDAO = purchaseDAO;
    }

    public Map<String, Object> verifyAndPurchase(String userId, PurchaseRequestDTO request) {
        MemberGamer gamer = (MemberGamer) userDAO.findById(userId);
        gamer.loadCart(cartDAO);
        
        // 1. 도메인 객체를 활용한 영수증(Purchase) 생성 및 READY 상태로 영속화
        Purchase purchase = Purchase.createFromCart(request.getOrderId(), userId, gamer.getCart(), purchaseDAO);
        purchaseDAO.save(purchase);

        // 2. 크로스 체크 (위변조 검증) - 도메인 객체에게 검증 위임 (실패 시 스스로 fail 처리 및 예외 던짐)
        purchase.verifyAmount(request.getAmount());

        // 3. 토스페이먼츠 승인(Confirm) API 서버 투 서버 통신
        try {
            RestTemplate restTemplate = new RestTemplate();
            String tossConfirmUrl = "https://api.tosspayments.com/v1/payments/confirm";
            
            HttpHeaders headers = new HttpHeaders();
            String secretKey = "test_sk_zXLkKEypNArWmo50nX3lmeaxYG5R"; 
            String encodedAuth = Base64.getEncoder().encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8));
            
            headers.set("Authorization", "Basic " + encodedAuth);
            headers.set("Content-Type", "application/json");

            Map<String, Object> params = new HashMap<>();
            params.put("paymentKey", request.getPaymentKey());
            params.put("orderId", request.getOrderId());
            params.put("amount", request.getAmount());

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(params, headers);
            ResponseEntity<Map> response = restTemplate.exchange(tossConfirmUrl, HttpMethod.POST, requestEntity, Map.class);
            
            if (response.getStatusCode() != HttpStatus.OK) {
                purchase.fail(); // 도메인 객체 스스로 실패 상태 전이 및 DB 동기화
                throw new IllegalStateException("결제 승인에 실패했습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            purchase.fail(); // 도메인 객체 스스로 실패 상태 전이 및 DB 동기화
            throw new IllegalStateException("결제 승인 중 통신 오류가 발생했습니다.");
        }

        // 4. 검증 통과! 결제 완료 상태로 전이 (도메인 객체 스스로 DB 업데이트)
        purchase.complete();

        // 5. 라이브러리에 게임 추가 및 장바구니 비우기 (Cart 객체에게 비우기 위임)
        for (Game game : gamer.getCart().getGames()) {
            libraryDAO.addGame(userId, game.getGameId());
        }
        gamer.getCart().clear();

        // 6. API 명세서에 맞게 응답 데이터 조립
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("transactionId", request.getPaymentKey());
        responseData.put("orderId", request.getOrderId());
        responseData.put("totalPaidAmount", purchase.getTotalAmount());
        return responseData;
    }
}