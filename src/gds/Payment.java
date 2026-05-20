package gds;

public class Payment {
    private final String paymentId;
    private final String methodName;
    private final int amount;
    private boolean paid;

    public Payment(String paymentId, String methodName, int amount) {
        this.paymentId = paymentId;
        this.methodName = methodName;
        this.amount = amount;
        this.paid = GConstant.Payment.INITIAL_PAID;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getMethodName() {
        return methodName;
    }

    public int getAmount() {
        return amount;
    }

    public boolean isPaid() {
        return paid;
    }

    public void pay() {
        this.paid = GConstant.Payment.PAID;
    }
}
