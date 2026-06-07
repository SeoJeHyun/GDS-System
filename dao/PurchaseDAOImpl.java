package dao;

import gds.Game;
import gds.Purchase;
import java.sql.*;

public class PurchaseDAOImpl implements PurchaseDAO {
    private final Connection conn;
    private final GameDAO gameDAO;

    public PurchaseDAOImpl(Connection conn, GameDAO gameDAO) {
        this.conn = conn;
        this.gameDAO = gameDAO;
    }

    @Override
    public void save(Purchase purchase) {
        // 트랜잭션 처리가 권장되는 지점이지만, DAO 레벨에서 순차적으로 Insert를 수행합니다.
        String insertPurchaseSql = "INSERT INTO purchases (purchase_id, user_id, purchase_status, total_amount) VALUES (?, ?, ?, ?)";
        String insertPurchaseGamesSql = "INSERT INTO purchase_games (purchase_id, game_id, price_at_purchase) VALUES (?, ?, ?)";

        try {
            // 1. purchases 부모 테이블에 결제 기본 정보 삽입
            try (PreparedStatement pstmt1 = conn.prepareStatement(insertPurchaseSql)) {
                pstmt1.setString(1, purchase.getPurchaseId());
                pstmt1.setString(2, purchase.getUserId());
                pstmt1.setString(3, purchase.getStatus());
                pstmt1.setInt(4, purchase.getTotalAmount());
                pstmt1.executeUpdate();
            }

            // 2. purchase_games 자식 테이블에 구매한 게임 목록(스냅샷) 삽입
            try (PreparedStatement pstmt2 = conn.prepareStatement(insertPurchaseGamesSql)) {
                for (Game game : purchase.getPurchasedGames()) {
                    pstmt2.setString(1, purchase.getPurchaseId());
                    pstmt2.setString(2, game.getGameId());
                    pstmt2.setInt(3, game.getPrice()); // 구매 당시의 가격 기록
                    pstmt2.addBatch(); // 여러 게임을 한 번에 넣기 위해 Batch 사용
                }
                pstmt2.executeBatch();
            }
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

    @Override
    public Purchase findById(String purchaseId) {
        String sql = "SELECT * FROM purchases WHERE purchase_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, purchaseId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // 조회 로직의 경우, 필요에 따라 PurchaseEntity로 매핑 후 Purchase.toDomain 호출
                    // 현재는 기본적인 뼈대만 제공합니다.
                    return new Purchase(rs.getString("purchase_id"), rs.getString("user_id"), 
                                        rs.getInt("total_amount"), rs.getString("purchase_status"), 
                                        null, this);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}