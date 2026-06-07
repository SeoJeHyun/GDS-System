package dao;

import gds.Game;
import java.util.List;

public interface CartDAO {
    // 1. 유저의 장바구니에 있는 모든 게임 불러오기
    List<Game> findCartGamesByUserId(String userId);
    
    // 2. 장바구니에 게임 추가하기 (DB 동기화)
    void addCartItem(String userId, String gameId);
    
    // 3. 결제 완료 후 장바구니 전체 비우기 (DB 동기화)
    void deleteAll(String userId);

    // 4. 장바구니에서 특정 게임 삭제 (DB 동기화)
    void delete(String userId, String gameId);
}