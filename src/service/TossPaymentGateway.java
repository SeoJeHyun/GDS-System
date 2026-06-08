package service;

public interface TossPaymentGateway {
    boolean confirmPayment(String paymentKey, String orderId, int amount);
}
