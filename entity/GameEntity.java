package entity;

public class GameEntity {
    private String gameId;
    private String title;
    private String developerId;
    private String developerName;
    private int price;
    private String genre;
    private boolean demoAvailable;
    private String detail;
    private String filePath; // 다운로드 URL (GameFile 객체 대체)
    private int fileSizeMb;
    private String deploymentStatus; // 문자열 상태값 (DistributionStatus 객체 대체)

    public GameEntity(String gameId, String title, String developerId, String developerName, 
                      int price, String genre, boolean demoAvailable, String detail, 
                      String filePath, int fileSizeMb, String deploymentStatus) {
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
    }

    public String getGameId() { return gameId; }
    public String getTitle() { return title; }
    public String getDeveloperId() { return developerId; }
    public String getDeveloperName() { return developerName; }
    public int getPrice() { return price; }
    public String getGenre() { return genre; }
    public boolean isDemoAvailable() { return demoAvailable; }
    public String getDetail() { return detail; }
    public String getFilePath() { return filePath; }
    public int getFileSizeMb() { return fileSizeMb; }
    public String getDeploymentStatus() { return deploymentStatus; }
}