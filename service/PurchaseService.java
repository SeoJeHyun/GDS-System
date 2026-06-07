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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
        
        // 1. 크로스 체크 (위변조 검증) - DB 장바구니의 진짜 총합을 계산
        int realTotalAmount = 0;
        for (Game game : gamer.getCart().getGames()) {
            realTotalAmount += game.getPrice();
        }

        if (realTotalAmount != request.getAmount()) {
            // TODO: 실제로는 여기서 토스페이먼츠(PG) 결제 취소 API를 호출합니다.
            throw new IllegalStateException("🚨 결제 금액 위변조가 감지되었습니다. 강제 환불 처리됩니다!");
        }

        // 2. 검증 통과! 영수증(Purchase) 발급 및 DB 저장
        String newPurchaseId = "tx_" + UUID.randomUUID().toString().substring(0, 8);
        Purchase purchase = new Purchase(newPurchaseId, userId, realTotalAmount, "COMPLETED", gamer.getCart().getGames(), purchaseDAO);
        purchaseDAO.save(purchase);

        // 3. 라이브러리에 게임 추가 및 장바구니 비우기
        for (Game game : gamer.getCart().getGames()) {
            libraryDAO.addGame(userId, game.getGameId());
        }
        cartDAO.deleteAll(userId);

        // 4. API 명세서에 맞게 응답 데이터 조립
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("transactionId", request.getPaymentKey());
        responseData.put("orderId", request.getOrderId());
        responseData.put("totalPaidAmount", realTotalAmount);
        return responseData;
    }
}