package dao;

import entity.PaymentEntity;
import gds.Payment;
import java.sql.*;

public class PaymentDAOImpl implements PaymentDAO {
    private final Connection conn;
    public PaymentDAOImpl(Connection conn) { this.conn = conn; }

    @Override
    public void save(Payment payment) {
        String sql = "INSERT INTO payments (payment_id, purchase_id, payment_method_id, amount, payment_status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // 여기에 payment 객체의 필드를 바인딩하는 코드를 작성하세요.
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void updateStatus(String paymentId, String status) {
        String sql = "UPDATE payments SET payment_status = ? WHERE payment_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setString(2, paymentId);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    // findByPurchaseId 등은 필요에 따라 추가 구현
    @Override public java.util.List<Payment> findByPurchaseId(String purchaseId) { return null; }
}