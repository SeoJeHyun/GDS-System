package gds;

public class Game {
    private final String gameId;
    private final String title;
    private final String developerName;
    private final int price;
    private final String genre;
    private final boolean demoAvailable;
    private final String detail;
    private final GameFile gameFile;
    private final DistributionStatus distributionStatus;

    public Game(
            String gameId,
            String title,
            String developerName,
            int price,
            String genre,
            boolean demoAvailable,
            String detail,
            GameFile gameFile,
            DistributionStatus distributionStatus
    ) {
        this.gameId = gameId;
        this.title = title;
        this.developerName = developerName;
        this.price = price;
        this.genre = genre;
        this.demoAvailable = demoAvailable;
        this.detail = detail;
        this.gameFile = gameFile;
        this.distributionStatus = distributionStatus;
    }

    public String getGameId() {
        return gameId;
    }

    public String getTitle() {
        return title;
    }

    public String getDeveloperName() {
        return developerName;
    }

    public int getPrice() {
        return price;
    }

    public String getGenre() {
        return genre;
    }

    public boolean isDemoAvailable() {
        return demoAvailable;
    }

    public String getDetail() {
        return detail;
    }

    public GameFile getGameFile() {
        return gameFile;
    }

    public DistributionStatus getDistributionStatus() {
        return distributionStatus;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Game)) {
            return false;
        }
        Game game = (Game) object;
        return gameId.equals(game.gameId);
    }

    @Override
    public int hashCode() {
        return gameId.hashCode();
    }
}
