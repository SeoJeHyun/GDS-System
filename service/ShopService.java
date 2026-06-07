package service;

import dao.GameDAO;
import dto.GameResponseDTO;
import gds.Game;

import java.util.ArrayList;
import java.util.List;

public class ShopService {
    private final GameDAO gameDAO;

    // 생성자로 GameDAO(창고지기)를 주입받습니다.
    public ShopService(GameDAO gameDAO) {
        this.gameDAO = gameDAO;
    }

    /**
     * 전체 게임 목록 조회
     * DB에서 모든 게임을 꺼내와, Main(UI)으로 내보내기 위해 안전한 DTO로 변환하여 반환합니다.
     */
    public List<GameResponseDTO> getAllGames() {
        List<Game> games = gameDAO.findAll();
        List<GameResponseDTO> responseList = new ArrayList<>();
        
        for (Game game : games) {
            responseList.add(game.toDTO());
        }
        return responseList;
    }

    /**
     * 게임 검색 기능
     * 키워드가 포함된 게임들만 찾아 DTO 리스트로 반환합니다.
     */
    public List<GameResponseDTO> searchGames(String keyword) {
        // 원래는 GameDAO에 findByTitleContaining 기능이 구현되어 있어야 합니다.
        // 현재는 DB 연결 뼈대만 있으므로, 우선 findAll()에서 메모리 필터링을 하는 방식으로 임시 구현합니다.
        List<Game> allGames = gameDAO.findAll();
        List<GameResponseDTO> result = new ArrayList<>();
        
        for (Game game : allGames) {
            if (game.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                result.add(game.toDTO());
            }
        }
        return result;
    }

    /**
     * 게임 상세 정보 조회 (엔티티를 그대로 반환하거나 DTO로 반환)
     * 장바구니에 담기 전 'Game' 도메인 객체 자체가 필요할 수 있으므로 도메인을 반환합니다.
     */
    public Game getGameDetail(String gameId) {
        Game game = gameDAO.findById(gameId);
        
        if (game == null) {
            System.out.println("오류: 해당 ID의 게임을 찾을 수 없습니다.");
        }
        return game;
    }
}