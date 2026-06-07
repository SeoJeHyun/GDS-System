package dao;

import gds.Game;
import java.util.List;

public interface LibraryDAO {
    // 1. 유저의 라이브러리에 있는 모든 게임 불러오기
    List<Game> findGamesByUserId(String userId);
    
    // 2. 라이브러리에 게임 영구 추가하기 (DB 동기화)
    void addGame(String userId, String gameId);
}