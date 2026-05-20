package dto;

// 화면에 뿌려줄 정보만 담는 순수한 데이터 컨테이너
public class GameResponseDTO {
    private String gameId;
    private String title;
    private String developerName;
    private int price;
    private String genre;

    // DTO는 로직이 없고, 오직 데이터를 담고 꺼내는 생성자와 Getter만 가집니다.
    public GameResponseDTO(String gameId, String title, String developerName, int price, String genre) {
        this.gameId = gameId;
        this.title = title;
        this.developerName = developerName;
        this.price = price;
        this.genre = genre;
    }

    public String getGameId() { return gameId; }
    public String getTitle() { return title; }
    public String getDeveloperName() { return developerName; }
    public int getPrice() { return price; }
    public String getGenre() { return genre; }
    
    // UI에서 목록을 출력할 때 편하게 쓰기 위한 toString 오버라이딩
    @Override
    public String toString() {
        return String.format("[%s] %s | 개발사: %s | 가격: %d원 | 장르: %s", 
                             gameId, title, developerName, price, genre);
    }
}