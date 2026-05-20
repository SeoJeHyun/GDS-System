package gds;

public class Purchase {
    private final MemberGamer purchaser;
    private final Game game;
    private final Payment payment;
    private boolean completed;

    public Purchase(MemberGamer purchaser, Game game, Payment payment) {
        this.purchaser = purchaser;
        this.game = game;
        this.payment = payment;
        this.completed = GConstant.Purchase.INITIAL_COMPLETED;
    }

    public MemberGamer getPurchaser() {
        return purchaser;
    }

    public Game getGame() {
        return game;
    }

    public Payment getPayment() {
        return payment;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void completePurchase() {
        payment.pay();
        completed = payment.isPaid();
    }
}
