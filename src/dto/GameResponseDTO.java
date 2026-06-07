package dto;

public class GameResponseDTO {
    private String gameId;
    private String title;
    private String developerName;
    private int price;
    private String genre;
    private String distributionStatus;

    public GameResponseDTO(String gameId, String title, String developerName, 
                           int price, String genre, String distributionStatus) {
        this.gameId = gameId;
        this.title = title;
        this.developerName = developerName;
        this.price = price;
        this.genre = genre;
        this.distributionStatus = distributionStatus;
    }

    public String getGameId() { return gameId; }
    public String getTitle() { return title; }
    public String getDeveloperName() { return developerName; }
    public int getPrice() { return price; }
    public String getGenre() { return genre; }
    public String getDistributionStatus() { return distributionStatus; }
}