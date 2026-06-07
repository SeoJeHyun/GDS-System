package service;

import dao.GameDAO;
import dao.LibraryDAO;
import dao.UserDAO;
import gds.Game;
import gds.MemberGamer;
import gds.User;

import java.util.ArrayList;
import java.util.List;

public class LibraryService {
    private final UserDAO userDAO;
    private final LibraryDAO libraryDAO;
    private final GameDAO gameDAO;

    // 창고지기들을 주입받습니다.
    public LibraryService(UserDAO userDAO, LibraryDAO libraryDAO, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.libraryDAO = libraryDAO;
        this.gameDAO = gameDAO;
    }

    // [유저 API 2. 각 유저별 구매한 게임 목록]
    public List<Game> getLibraryGames(String userId, int page, int size) {
        MemberGamer gamer = getAssembledGamer(userId);
        List<Game> allGames = gamer.getLibrary().getGames();

        // API 명세에 따른 메모리 페이징 처리 (요청한 페이지 범위를 잘라서 반환)
        int fromIndex = (page - 1) * size;
        if (allGames == null || allGames.size() <= fromIndex) {
            return new ArrayList<>();
        }
        int toIndex = Math.min(fromIndex + size, allGames.size());
        return allGames.subList(fromIndex, toIndex);
    }

    // [유저 API 7. 구매한 게임 파일 다운로드 요청]
    public Game downloadGame(String userId, String gameId) {
        MemberGamer gamer = getAssembledGamer(userId);
        
        // 💡 [도메인 위임] 내 라이브러리에 정말 있는지 스스로 검증하게 시킵니다.
        if (!gamer.getLibrary().hasGame(gameId)) {
            throw new IllegalStateException("구매하지 않은 게임은 다운로드할 수 없습니다.");
        }

        // 검증이 통과되면, 게임의 상세 정보(파일 경로 포함)를 반환하여 Controller가 텍스트를 만들어주도록 돕습니다.
        return gameDAO.findById(gameId);
    }

    // --- 헬퍼 메서드: MemberGamer 조립 ---
    private MemberGamer getAssembledGamer(String userId) {
        User user = userDAO.findById(userId);
        if (!(user instanceof MemberGamer)) {
            throw new IllegalStateException("회원 게이머만 라이브러리 기능을 이용할 수 있습니다.");
        }
        MemberGamer gamer = (MemberGamer) user;
        // 장바구니 로드는 불필요하므로 라이브러리만 스스로 로드하도록 도구를 쥐여줍니다.
        gamer.loadLibrary(libraryDAO);
        return gamer;
    }
}