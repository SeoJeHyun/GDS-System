package service;

import dao.LibraryDAO;
import dao.UserDAO;
import dto.GameResponseDTO;
import gds.Game;
import gds.MemberGamer;
import gds.User;

import java.util.List;
import java.util.stream.Collectors;

public class LibraryService {
    private final UserDAO userDAO;
    private final LibraryDAO libraryDAO;

    public LibraryService(UserDAO userDAO, LibraryDAO libraryDAO) {
        this.userDAO = userDAO;
        this.libraryDAO = libraryDAO;
    }

    public List<GameResponseDTO> getUserLibrary(String userId) {
        User user = userDAO.findById(userId);
        if (!(user instanceof MemberGamer)) {
            throw new IllegalStateException("게이머 회원만 라이브러리를 조회할 수 있습니다.");
        }
        MemberGamer gamer = (MemberGamer) user;
        
        // 💡 [도메인 지능 활용] 객체 스스로 라이브러리를 세팅하게 위임
        gamer.loadLibrary(libraryDAO);

        // 라이브러리 안의 게임들을 안전한 DTO로 포장하여 반환
        return gamer.getLibrary().getGames().stream()
                .map(Game::toDTO)
                .collect(Collectors.toList());
    }

    public String getGameDownloadContent(String userId, String gameId) {
        User user = userDAO.findById(userId);
        if (!(user instanceof MemberGamer)) throw new IllegalStateException("게이머 회원만 다운로드 가능합니다.");
        
        MemberGamer gamer = (MemberGamer) user;
        gamer.loadLibrary(libraryDAO);
        
        if (!gamer.getLibrary().hasGame(gameId)) throw new IllegalStateException("구매하지 않은 게임은 다운로드할 수 없습니다.");

        return "Game ID: " + gameId + "\n\n이 파일은 GDS 시스템에서 정상적으로 결제 후 다운로드된 게임 파일(대체 텍스트)입니다!\n재밌게 즐겨주세요!";
    }
}