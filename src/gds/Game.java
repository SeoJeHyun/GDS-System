package gds;

/*
 *
 * 1. 장르(Genre)의 N:M 관계 분리
 * - String genre 하나로 퉁치지 말고, 한 게임이 여러 장르를 가질 수 있도록 List<Genre>로 변경.
 * - 실제 DB에서는 '게임_장르_매핑' 다대다 테이블로 설계할 것.
 *
 * 2. 객체 업데이트 허용 (패치 반영)
 * - GameFile을 교체(패치 업데이트)하거나 가격이 변동될 수 있으므로, 내부 필드들의 final을 제거.
 * - 실제 게임 파일은 외부 스토리지(S3 등)에 두고, GameFile 객체는 다운로드 URL, 용량, 버전 정보(메타데이터)만 담도록 역할 축소.
 *
 * 3. Builder 패턴 도입
 * - 필드가 많아 생성자가 비대해지므로, 객체 생성의 가독성과 안전성을 위해 빌더(Builder) 패턴을 적용할 것.
 */
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
