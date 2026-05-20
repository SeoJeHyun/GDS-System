package dto;

public class CartItemDTO {
    private String gameId;
    private String title;
    private int price;

    public CartItemDTO(String gameId, String title, int price) {
        this.gameId = gameId;
        this.title = title;
        this.price = price;
    }

    public String getGameId() { return gameId; }
    public String getTitle() { return title; }
    public int getPrice() { return price; }

    // 장바구니 목록 출력용 포맷
    @Override
    public String toString() {
        return String.format("- %s (가격: %d원)", title, price);
    }
}
