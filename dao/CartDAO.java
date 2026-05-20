package dao;

import gds.Game;
import java.util.List;

public interface CartDAO {
    // 1. 장바구니에 게임 담기 (userId와 gameId의 연결 정보 저장)
    void addGameToCart(String userId, String gameId);

    // 2. 장바구니에서 특정 게임 빼기
    void removeGameFromCart(String userId, String gameId);

    // 3. 특정 유저가 장바구니에 담아둔 모든 게임 목록 불러오기
    List<Game> findCartItemsByUserId(String userId);

    // 4. 장바구니 비우기 (결제가 성공적으로 완료된 후 호출됨)
    void clearCart(String userId);
}