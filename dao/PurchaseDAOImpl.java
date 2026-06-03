package dao;

import entity.PurchaseEntity;
import gds.Purchase;
import java.sql.*;

public class PurchaseDAOImpl implements PurchaseDAO {
    private final Connection conn;

    public PurchaseDAOImpl(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void save(Purchase purchase) {
        // SQL 문장을 한 줄로 깔끔하게 연결했습니다.
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
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // 인터페이스 규격을 맞추기 위해 필수적인 나머지 메서드들
    @Override
    public Purchase findById(String purchaseId) {
        return null; // 필요시 구현
    }

    @Override
    public java.util.List<Purchase> findByUserId(String userId) {
        return null; // 필요시 구현
    }
}