package gds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
1. 현재 상태 정의
- 현재 Cart 클래스의 List<Game>은 UI 출력 및 중복 검사를 위한 '메모리 버퍼' 역할을 수행
- Cart 클래스 자체에 DB 통신 로직을 넣지 않고, 순수하게 상태만 유지하는 객체로 남겨둘것

2. DB 동기화 통신 시점 정의
- addGame()이나 removeGame()이 호출도리 때, 단독으로 동작하게 두지 말것
-해당 메서드가 호출되는 부분에서 메모리를 업데이트함과 동시에 CartDAO를 통해 DB에도 변경사항이
저장되게 할 것

3. DB 테이블 매핑(N:M 관계)
- 관계형 DB에서는 Cart 테이블을 따로 두기보다, 사용자(user_id)와 게임(game_id)를 연결하는
다대다 매핑 테이블로 풀어내는 것이 일반적
 */

public class Cart {
    private final List<Game> games;

    public Cart() {
        this.games = new ArrayList<>();
    }

    public void addGame(Game game) {
        if (!games.contains(game)) {
            games.add(game);
        }
    }

    public void removeGame(Game game) {
        games.remove(game);
    }

    public List<Game> getGames() {
        return Collections.unmodifiableList(games);
    }

    public boolean contains(Game game) {
        return games.contains(game);
    }
}
