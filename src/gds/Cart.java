package gds;

import dao.CartDAO;
import java.util.ArrayList;
import java.util.List;

public class Cart {
    private final String userId;
    private final List<Game> games;
    private final CartDAO cartDAO;

    public Cart(String userId, List<Game> games, CartDAO cartDAO) {
        this.userId = userId;
        this.games = new ArrayList<>(games); // 안전한 복사본 저장
        this.cartDAO = cartDAO;
    }

    // [풍부한 도메인 모델] 1. 장바구니 총액 스스로 계산
    public int getTotalAmount() {
        int total = 0;
        for (Game game : games) {
            total += game.getPrice();
        }
        return total;
    }

    // [풍부한 도메인 모델] 2. 결제 완료 후 스스로 장바구니 비우기
    public void clear() {
        this.games.clear();
        this.cartDAO.clearCart(this.userId); // DB 동기화
    }

    // [풍부한 도메인 모델] 3. 장바구니에 게임 추가 (중복 검증 포함)
    public void addGame(Game game) {
        for (Game g : games) {
            if (g.getGameId().equals(game.getGameId())) {
                throw new IllegalStateException("이미 장바구니에 담긴 게임입니다.");
            }
        }
        
        this.games.add(game); // 메모리 추가
        this.cartDAO.addCartItem(this.userId, game.getGameId()); // DB 동기화
    }

    // 외부에서 데이터를 마음대로 조작하지 못하도록 방어적 복사본 반환
    public List<Game> getGames() {
        return new ArrayList<>(this.games);
    }
}