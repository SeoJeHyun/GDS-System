package dto;

public class CartRequestDTO {
    private String gameId;

    public CartRequestDTO(String gameId) {
        this.gameId = gameId;
    }

    public String getGameId() { return gameId; }
}