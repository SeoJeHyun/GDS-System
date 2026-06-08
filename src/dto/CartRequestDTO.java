package dto;

public class CartRequestDTO {
    private String userId; // 임시: 프론트엔드에서 직접 userId를 보냄
    private String gameId;

    public CartRequestDTO() {}

    public CartRequestDTO(String userId, String gameId) {
        this.userId = userId;
        this.gameId = gameId;
    }

    public String getUserId() { return userId; }
    public String getGameId() { return gameId; }
}