package dao;

import entity.GameEntity;
import gds.Game;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameDAOImpl implements GameDAO {
    private final Connection conn;

    public GameDAOImpl(Connection conn) { this.conn = conn; }

    @Override
    public Game findById(String gameId) {
        // 💡 역정규화된 최신 games 스키마 단일 조회
        String sql = "SELECT * FROM games WHERE game_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, gameId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Game.toDomain(mapRow(rs), this);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    private GameEntity mapRow(ResultSet rs) throws SQLException {
        return new GameEntity(rs.getString("game_id"), rs.getString("title"), rs.getString("developer_id"),
                              rs.getString("developer_name"), rs.getInt("price"), rs.getString("genre"),
                              rs.getBoolean("demo_available"), rs.getString("detail"), 
                              rs.getString("file_path"), 0, rs.getString("status_name")); // 최신 DB 스키마 완벽 반영
    }

    @Override public List<Game> findAll() { return new ArrayList<>(); }
    @Override public void save(Game game) {}
    @Override public void update(Game game) {}
    @Override public List<Game> findByTitleContaining(String keyword) { return null; }
    @Override public List<Game> findByGenre(String genre) { return null; }
    @Override public void delete(String gameId) {}
}
