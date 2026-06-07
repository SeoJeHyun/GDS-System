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

    @Override 
    public List<Game> findAll() {
        List<Game> games = new ArrayList<>();
        String sql = "SELECT * FROM games";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                games.add(Game.toDomain(mapRow(rs), this));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return games;
    }

    @Override 
    public void save(Game game) {
        String sql = "INSERT INTO games (game_id, title, developer_id, developer_name, price, genre, demo_available, detail, file_path, status_name) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, game.getGameId());
            pstmt.setString(2, game.getTitle());
            pstmt.setString(3, game.getDeveloperId());
            pstmt.setString(4, game.getDeveloperName());
            pstmt.setInt(5, game.getPrice());
            pstmt.setString(6, game.getGenre());
            pstmt.setBoolean(7, game.isDemoAvailable());
            pstmt.setString(8, game.getDetail());
            pstmt.setString(9, game.getFilePath());
            pstmt.setString(10, game.getDeploymentStatus());
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override 
    public void update(Game game) {
        String sql = "UPDATE games SET title=?, developer_id=?, developer_name=?, price=?, genre=?, demo_available=?, detail=?, file_path=?, status_name=? WHERE game_id=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, game.getTitle());
            pstmt.setString(2, game.getDeveloperId());
            pstmt.setString(3, game.getDeveloperName());
            pstmt.setInt(4, game.getPrice());
            pstmt.setString(5, game.getGenre());
            pstmt.setBoolean(6, game.isDemoAvailable());
            pstmt.setString(7, game.getDetail());
            pstmt.setString(8, game.getFilePath());
            pstmt.setString(9, game.getDeploymentStatus());
            pstmt.setString(10, game.getGameId());
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override 
    public List<Game> findByTitleContaining(String keyword) {
        List<Game> games = new ArrayList<>();
        String sql = "SELECT * FROM games WHERE title LIKE ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + keyword + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) { games.add(Game.toDomain(mapRow(rs), this)); }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return games;
    }

    @Override 
    public List<Game> findByGenre(String genre) {
        List<Game> games = new ArrayList<>();
        String sql = "SELECT * FROM games WHERE genre = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, genre);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) { games.add(Game.toDomain(mapRow(rs), this)); }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return games;
    }

    @Override 
    public void delete(String gameId) {
        String sql = "DELETE FROM games WHERE game_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, gameId);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
