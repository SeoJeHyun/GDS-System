package dao;

import entity.*;
import gds.*;
import java.sql.*;
import java.util.List;

public class UserDAOImpl implements UserDAO {
    private final Connection conn;

    public UserDAOImpl(Connection conn) { this.conn = conn; }

    @Override
    public User findById(String userId) {
        String sql = "SELECT password, name, user_type FROM users WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // DB에서 꺼낸 정보로 Entity를 만들고, 도메인 공장(toDomain)으로 객체 부활
                    String type = rs.getString("user_type");
                    UserEntity entity = fetchSubtypeEntity(userId, rs.getString("password"), rs.getString("name"), type);
                    return User.toDomain(entity, this);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // (fetchSubtypeEntity는 이전 답변의 로직과 동일하므로 생략)
    @Override
    public void update(User user) { /* SQL UPDATE 로직 */ }
    @Override public void save(User user) {}
    @Override public List<User> findAll() { return null; }
    @Override public void delete(String userId) {}

    private UserEntity fetchSubtypeEntity(String userId, String password, String name, String userType) throws SQLException {
    if ("DEVELOPER".equals(userType)) {
        String devSql = "SELECT company_name FROM developers WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(devSql)) {
            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return new DeveloperEntity(userId, password, name, rs.getString("company_name"));
            }
        }
    } else if ("ADMINISTRATOR".equals(userType)) {
        String adminSql = "SELECT department FROM administrators WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(adminSql)) {
            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return new AdministratorEntity(userId, password, name, rs.getString("department"));
            }
        }
    } else if ("MEMBER_GAMER".equals(userType)) {
        return new MemberGamerEntity(userId, password, name);
    }
    throw new IllegalArgumentException("알 수 없는 유저 타입: " + userType);
}
}