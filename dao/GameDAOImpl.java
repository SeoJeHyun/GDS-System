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
        // 게임 정보와 파일 경로를 JOIN해서 가져옴
        String sql = "SELECT g.*, f.file_path FROM games g JOIN game_files f ON g.file_id = f.file_id WHERE g.game_id = ?";
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
                              rs.getBoolean("demo_available"), rs.getString("detail"), rs.getString("file_path"),
                              rs.getInt("file_size_mb"), rs.getString("deployment_status"));
    }

    @Override public List<Game> findAll() { return new ArrayList<>(); }
    @Override public void save(Game game) {}
    @Override public void update(Game game) {}
    @Override public List<Game> findByTitleContaining(String keyword) { return null; }
    @Override public List<Game> findByGenre(String genre) { return null; }
    @Override public void delete(String gameId) {}
}