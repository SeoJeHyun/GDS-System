package dao;

import entity.PaymentEntity;
import gds.Payment;
import java.sql.*;
import java.util.List;

public class PaymentDAOImpl implements PaymentDAO {
    private final Connection conn;

    public PaymentDAOImpl(Connection conn) { 
        this.conn = conn; 
    }

    @Override
    public void save(Payment payment) {
        String sql = "INSERT INTO payments (payment_id, purchase_id, payment_method_id, amount, payment_status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, payment.getPaymentId());
            pstmt.setString(2, payment.getPurchaseId());
            pstmt.setString(3, payment.getPaymentMethodId());
            pstmt.setInt(4, payment.getAmount());
            pstmt.setString(5, "READY");
            pstmt.executeUpdate();
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
    }

    @Override
    public void updateStatus(String paymentId, String status) {
        String sql = "UPDATE payments SET payment_status = ? WHERE payment_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setString(2, paymentId);
            pstmt.executeUpdate();
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
    }
    
    @Override 
    public List<Payment> findByPurchaseId(String purchaseId) { 
        return null; 
    }
}