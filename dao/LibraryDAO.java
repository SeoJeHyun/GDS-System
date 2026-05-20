package dao;


import gds.Game;
import java.util.List;

public interface LibraryDAO {
    // 1. 유저의 라이브러리에 구매 완료한 게임 추가
    void addGameToLibrary(String userId, String gameId);

    // 2. 특정 유저가 소유한(라이브러리에 있는) 모든 게임 목록 조회
    List<Game> findLibraryGamesByUserId(String userId);

    // 3. 유저가 이미 해당 게임을 소유하고 있는지 확인 (중복 구매 방지용)
    boolean hasGame(String userId, String gameId);
}