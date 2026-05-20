package gds;

public class MemberGamer extends User {
    private final Cart cart;
    private final Library library;

    public MemberGamer(String userId, String password, String name) {
        super(userId, password, name);
        this.cart = new Cart();
        this.library = new Library();
    }

    public Cart getCart() {
        return cart;
    }

    public Library getLibrary() {
        return library;
    }

    public void addGameToCart(Game game) {
        cart.addGame(game);
    }

    public Purchase purchase(Game game, Payment payment) {
        Purchase purchase = new Purchase(this, game, payment);
        purchase.completePurchase();
        library.addGame(game);
        cart.removeGame(game);
        return purchase;
    }

    @Override
    public String getUserType() {
        return GConstant.UserType.MEMBER_GAMER;
    }
}
