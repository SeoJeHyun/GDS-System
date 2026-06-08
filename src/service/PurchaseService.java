package service;

import dao.CartDAO;
import dao.LibraryDAO;
import dao.PurchaseDAO;
import dao.PaymentDAO;
import dao.UserDAO;
import dto.PurchaseRequestDTO;
import gds.Game;
import gds.MemberGamer;
import gds.Payment;
import gds.Purchase;
import gds.User;
import service.TossPaymentGateway;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PurchaseService {
    private final UserDAO userDAO;
    private final CartDAO cartDAO;
    private final LibraryDAO libraryDAO;
    private final PurchaseDAO purchaseDAO;
    private final PaymentDAO paymentDAO;
    private final TossPaymentGateway tossGateway;

    public PurchaseService(UserDAO userDAO, CartDAO cartDAO, LibraryDAO libraryDAO, PurchaseDAO purchaseDAO, PaymentDAO paymentDAO, TossPaymentGateway tossGateway) {
        this.userDAO = userDAO;
        this.cartDAO = cartDAO;
        this.libraryDAO = libraryDAO;
        this.purchaseDAO = purchaseDAO;
        this.paymentDAO = paymentDAO;
        this.tossGateway = tossGateway;
    }

    public Map<String, Object> verifyAndPurchase(String userId, PurchaseRequestDTO request) {
        MemberGamer gamer = (MemberGamer) userDAO.findById(userId);
        gamer.loadCart(cartDAO);
        
        // 1. 도메인 객체를 활용한 영수증(Purchase) 생성 및 READY 상태로 영속화
        Purchase purchase = Purchase.createFromCart(request.getOrderId(), userId, gamer.getCart(), purchaseDAO);
        purchaseDAO.save(purchase);

        // 2. 크로스 체크 (위변조 검증) - 도메인 객체에게 검증 위임 (실패 시 스스로 fail 처리 및 예외 던짐)
        purchase.verifyAmount(request.getAmount());

        // 3. 결제 트랜잭션(Payment) 객체 생성 및 토스 게이트웨이 위임
        String paymentId = "pay_" + UUID.randomUUID().toString().substring(0, 8);
        // 💡 "TOSS" 대신 DB(payment_methods 테이블)에 존재하는 "payment1"을 사용해야 외래키 제약조건(FK) 에러가 발생하지 않습니다.
        Payment payment = new Payment(paymentId, purchase.getPurchaseId(), "payment1", request.getAmount(), "READY", paymentDAO);
        paymentDAO.save(payment); // 결제 시도 이력 DB 안전 저장

        // Payment 도메인 스스로 토스 승인 처리 지시 (캡슐화 완벽 달성)
        boolean isSuccess = payment.processToss(tossGateway, request.getPaymentKey(), request.getOrderId());
        
        if (!isSuccess) {
            purchase.fail(); // 영수증도 실패 처리
            throw new IllegalStateException("결제 승인에 실패했습니다.");
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