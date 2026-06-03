package dao;

import entity.PurchaseEntity;
import gds.Purchase;
import java.sql.*;
import java.util.List;

public class PurchaseDAOImpl implements PurchaseDAO {
    private final Connection conn;

    public PurchaseDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void save(Purchase purchase) {
        String sql = "INSERT INTO purchases (purchase_id, user_id, total_amount, purchase_status) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, purchase.getPurchaseId());
            pstmt.setString(2, purchase.getUserId());
            pstmt.setInt(3, purchase.getTotalAmount());
            pstmt.setString(4, "READY"); // 초기 상태값
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateStatus(String purchaseId, String status) {
        String sql = "UPDATE purchases SET purchase_status = ? WHERE purchase_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setString(2, purchaseId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 인터페이스 규격을 맞추기 위한 메서드
    @Override
    public Purchase findById(String purchaseId) {
        return null; 
    }

    @Override
    public List<Purchase> findByUserId(String userId) {
        return null; 
    }
}