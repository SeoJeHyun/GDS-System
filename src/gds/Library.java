package gds;

import dao.LibraryDAO;
import java.util.ArrayList;
import java.util.List;

public class Library {
    private final String userId;
    private final List<Game> games;
    private final LibraryDAO libraryDAO;

    public Library(String userId, List<Game> games, LibraryDAO libraryDAO) {
        this.userId = userId;
        this.games = new ArrayList<>(games); // 방어적 복사
        this.libraryDAO = libraryDAO;
    }

    // [풍부한 도메인 모델] 특정 게임 소유 여부 스스로 판단
    public boolean hasGame(String gameId) {
        for (Game game : games) {
            if (game.getGameId().equals(gameId)) {
                return true;
            }
        }
        return false;
    }

    // [풍부한 도메인 모델] 라이브러리에 게임 추가 및 DB 동기화
    public void addGame(Game game) {
        if (hasGame(game.getGameId())) {
            throw new IllegalStateException("이미 라이브러리에 존재하는 게임입니다.");
        }
        this.games.add(game);
        this.libraryDAO.addGame(this.userId, game.getGameId());
    }

    public List<Game> getGames() {
        return new ArrayList<>(this.games);
    }
}