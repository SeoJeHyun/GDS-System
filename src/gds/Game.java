package gds;

import dao.GameDAO;
import dto.GameResponseDTO;
import entity.GameEntity;

public class Game {
    private final String gameId;
    private String title;
    private final String developerId;
    private final String developerName;
    private int price;
    private String genre;
    private boolean demoAvailable;
    private String detail;
    private String filePath;
    private int fileSizeMb;
    private String deploymentStatus;
    
    private final GameDAO gameDAO;

    public Game(String gameId, String title, String developerId, String developerName, 
                int price, String genre, boolean demoAvailable, String detail, 
                String filePath, int fileSizeMb, String deploymentStatus, GameDAO gameDAO) {
        this.gameId = gameId;
        this.title = title;
        this.developerId = developerId;
        this.developerName = developerName;
        this.price = price;
        this.genre = genre;
        this.demoAvailable = demoAvailable;
        this.detail = detail;
        this.filePath = filePath;
        this.fileSizeMb = fileSizeMb;
        this.deploymentStatus = deploymentStatus;
        this.gameDAO = gameDAO;
    }

    public String getGameId() { return gameId; }
    public String getTitle() { return title; }
    public int getPrice() { return price; }

    // 비즈니스 로직 (스스로 DB 업데이트)
    public void updatePrice(int newPrice) {
        if (newPrice < 0) throw new IllegalArgumentException("가격은 0 이상이어야 합니다.");
        this.price = newPrice;
        this.gameDAO.update(this);
    }

    public void updateDeploymentStatus(String newStatus) {
        this.deploymentStatus = newStatus;
        this.gameDAO.update(this);
    }

    public GameResponseDTO toDTO() {
        return new GameResponseDTO(gameId, title, developerName, price, genre, deploymentStatus);
    }

    // Entity -> Domain 변환 공장
    public static Game toDomain(GameEntity entity, GameDAO dao) {
        return new Game(
            entity.getGameId(), entity.getTitle(), entity.getDeveloperId(), entity.getDeveloperName(),
            entity.getPrice(), entity.getGenre(), entity.isDemoAvailable(), entity.getDetail(),
            entity.getFilePath(), entity.getFileSizeMb(), entity.getDeploymentStatus(), dao
        );
    }
}