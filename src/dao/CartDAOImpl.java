package dao;

import entity.GameEntity;
import gds.Game;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAOImpl implements CartDAO {
    private final Connection conn;
    private final GameDAO gameDAO; // Game 객체 조립을 위해 주입받음

    public CartDAOImpl(Connection conn, GameDAO gameDAO) {
        this.conn = conn;
        this.gameDAO = gameDAO;
    }

    @Override
    public List<Game> findCartGamesByUserId(String userId) {
        List<Game> cartGames = new ArrayList<>();
        // 💡 DB.md 스키마 완벽 반영: carts 테이블과 cart_items 테이블 조인
        String sql = "SELECT g.* FROM carts c " +
                     "JOIN cart_items ci ON c.cart_id = ci.cart_id " +
                     "JOIN games g ON ci.game_id = g.game_id " +
                     "WHERE c.user_id = ?";
                     
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    GameEntity entity = new GameEntity(
                        rs.getString("game_id"), rs.getString("title"), rs.getString("developer_id"),
                        rs.getString("developer_name"), rs.getInt("price"), rs.getString("genre"),
                        rs.getBoolean("demo_available"), rs.getString("detail"), 
                        rs.getString("file_path"), 0, rs.getString("status_name") // 최신 games 스키마 적용
                    );
                    cartGames.add(Game.toDomain(entity, gameDAO));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartGames;
    }

    @Override
    public void addCartItem(String userId, String gameId) {
        // 💡 DB.md 스키마 완벽 반영: user_id로 cart_id를 찾아서 아이템 추가 (서브쿼리 활용)
        String sql = "INSERT INTO cart_items (cart_id, game_id) " +
                     "SELECT cart_id, ? FROM carts WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, gameId);
            pstmt.setString(2, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void deleteAll(String userId) {
        // 특정 유저의 장바구니 번호(cart_id)에 해당하는 아이템 모두 삭제
        String sql = "DELETE FROM cart_items WHERE cart_id = (SELECT cart_id FROM carts WHERE user_id = ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public void delete(String userId, String gameId) {
        // 특정 유저의 장바구니에서 특정 게임 하나만 삭제
        String sql = "DELETE FROM cart_items WHERE cart_id = (SELECT cart_id FROM carts WHERE user_id = ?) AND game_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.setString(2, gameId);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}