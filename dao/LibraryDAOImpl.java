package dao;

import entity.GameEntity;
import gds.Game;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibraryDAOImpl implements LibraryDAO {
    private final Connection conn;
    private final GameDAO gameDAO;

    public LibraryDAOImpl(Connection conn, GameDAO gameDAO) {
        this.conn = conn;
        this.gameDAO = gameDAO;
    }

    @Override
    public List<Game> findGamesByUserId(String userId) {
        List<Game> libraryGames = new ArrayList<>();
        // 💡 DB.md 최신 스키마 완벽 반영: libraries 테이블과 조인 및 최신 games 컬럼 사용
        String sql = "SELECT g.* FROM libraries lib " +
                     "JOIN library_games lg ON lib.library_id = lg.library_id " +
                     "JOIN games g ON lg.game_id = g.game_id " +
                     "WHERE lib.user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    GameEntity entity = new GameEntity(
                        rs.getString("game_id"), rs.getString("title"), rs.getString("developer_id"),
                        rs.getString("developer_name"), rs.getInt("price"), rs.getString("genre"),
                        rs.getBoolean("demo_available"), rs.getString("detail"), 
                        rs.getString("file_path"), 0, rs.getString("status_name") // 변경된 스키마 이름 적용
                    );
                    libraryGames.add(Game.toDomain(entity, gameDAO));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return libraryGames;
    }

    @Override
    public void addGame(String userId, String gameId) {
        // 💡 DB.md 스키마 완벽 반영: user_id로 library_id를 먼저 찾아서 삽입하는 스마트한 서브쿼리!
        String sql = "INSERT INTO library_games (library_id, game_id) " +
                     "SELECT library_id, ? FROM libraries WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, gameId);
            pstmt.setString(2, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
