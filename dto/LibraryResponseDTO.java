package dto;

public class LibraryResponseDTO {
    private String gameId;
    private String title;
    private String genre;

    public LibraryResponseDTO(String gameId, String title, String genre) {
        this.gameId = gameId;
        this.title = title;
        this.genre = genre;
    }

    public String getGameId() { return gameId; }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }

    @Override
    public String toString() {
        return String.format("   ↳ [%s] %s (장르: %s) -> 실행 가능", gameId, title, genre);
    }
}