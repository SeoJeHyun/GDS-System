package service;

import dao.CartDAO;
import dao.LibraryDAO;
import dao.PurchaseDAO;
import dao.UserDAO;
import gds.Cart;
import gds.Game;
import gds.MemberGamer;
import gds.Purchase;
import gds.User;

public class PurchaseService {
    private final UserDAO userDAO;
    private final CartDAO cartDAO;
    private final LibraryDAO libraryDAO;
    private final PurchaseDAO purchaseDAO;

    // 필요한 창고지기들을 주입받습니다. (외부 PG 연동은 추후 도입)
    public PurchaseService(UserDAO userDAO, CartDAO cartDAO, LibraryDAO libraryDAO, PurchaseDAO purchaseDAO) {
        this.userDAO = userDAO;
        this.cartDAO = cartDAO;
        this.libraryDAO = libraryDAO;
        this.purchaseDAO = purchaseDAO;
    }

    // [API 4. 장바구니 결제 요청] 지휘 로직
    public Purchase processPurchase(String userId, String orderId, int pgAmount) {
        // 1. 유저 도메인 조립 (스스로 로드)
        MemberGamer gamer = getAssembledGamer(userId);
        Cart cart = gamer.getCart();

        if (cart.getGames().isEmpty()) {
            throw new IllegalStateException("장바구니가 비어 있어 결제할 수 없습니다.");
        }

        // 2. 주문 객체(Purchase) 생성 및 동결 (DB 저장)
        Purchase purchase = Purchase.createFromCart(orderId, userId, cart, purchaseDAO);
        purchaseDAO.save(purchase);

        try {
            // 3. [도메인 위임] 프론트엔드가 보낸 금액(pgAmount)과 장바구니 진짜 총액 교차 검증
            purchase.verifyAmount(pgAmount);
            
            // (임시) 검증을 통과했으므로 결제 승인 성공으로 간주합니다.
            
            // 4. [도메인 위임] 결제 완료 처리
            purchase.complete();
            
            // 5. [도메인 위임] 결제된 게임들을 유저의 라이브러리에 추가 (Library 객체가 스스로 DB 동기화)
            for (Game game : cart.getGames()) {
                gamer.getLibrary().addGame(game); 
            }
            
            // 6. [도메인 위임] 결제가 끝났으니 장바구니 비우기 (Cart 객체가 스스로 DB 동기화)
            cart.clear();
            
            return purchase;
            
        } catch (Exception e) {
            // 검증 실패 시: 주문 실패 처리
            purchase.fail();
            throw e;
        }
    }

    // --- 헬퍼 메서드: MemberGamer 조립 ---
    private MemberGamer getAssembledGamer(String userId) {
        User user = userDAO.findById(userId);
        if (!(user instanceof MemberGamer)) {
            throw new IllegalStateException("회원 게이머만 결제할 수 있습니다.");
        }
        MemberGamer gamer = (MemberGamer) user;
        gamer.loadCart(cartDAO);
        gamer.loadLibrary(libraryDAO);
        return gamer;
    }
}
