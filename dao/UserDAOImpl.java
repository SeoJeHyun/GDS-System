package dao;

import entity.*;
import gds.*;
import java.sql.*;
import java.util.ArrayList;
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
    public void update(User user) {
        String sql = "UPDATE users SET name = ?, password = ? WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getPasswordForDAO());
            pstmt.setString(3, user.getUserId());
            pstmt.executeUpdate();
            
            // 서브타입에 따른 추가 정보 업데이트
            if (user instanceof Developer) {
                String devSql = "UPDATE developers SET company_name = ? WHERE user_id = ?";
                try (PreparedStatement dpstmt = conn.prepareStatement(devSql)) {
                    dpstmt.setString(1, ((Developer) user).getCompanyName());
                    dpstmt.setString(2, user.getUserId());
                    dpstmt.executeUpdate();
                }
            } else if (user instanceof Administrator) {
                String adminSql = "UPDATE administrators SET department = ? WHERE user_id = ?";
                try (PreparedStatement apstmt = conn.prepareStatement(adminSql)) {
                    apstmt.setString(1, ((Administrator) user).getDepartment());
                    apstmt.setString(2, user.getUserId());
                    apstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override 
    public void save(User user) {
        String insertUserSql = "INSERT INTO users (user_id, password, name, user_type) VALUES (?, ?, ?, ?)";
        String insertGamerSql = "INSERT INTO member_gamers (user_id) VALUES (?)";
        String insertCartSql = "INSERT INTO carts (user_id) VALUES (?)";
        String insertLibrarySql = "INSERT INTO libraries (user_id) VALUES (?)";

        try {
            // 1. 공통 유저 정보 (users) 저장
            try (PreparedStatement pstmt = conn.prepareStatement(insertUserSql)) {
                pstmt.setString(1, user.getUserId());
                pstmt.setString(2, user.getPasswordForDAO());
                pstmt.setString(3, user.getName());
                pstmt.setString(4, mapToDBUserType(user.getUserType())); // 💡 DB 제약조건 충돌 방지 매핑
                pstmt.executeUpdate();
            }

            // 2. 서브타입에 따른 추가 데이터 완벽 저장 처리
            if (user instanceof MemberGamer) {
                try (PreparedStatement pstmt = conn.prepareStatement(insertGamerSql)) {
                    pstmt.setString(1, user.getUserId());
                    pstmt.executeUpdate();
                }
                try (PreparedStatement pstmt = conn.prepareStatement(insertCartSql)) {
                    pstmt.setString(1, user.getUserId());
                    pstmt.executeUpdate();
                }
                try (PreparedStatement pstmt = conn.prepareStatement(insertLibrarySql)) {
                    pstmt.setString(1, user.getUserId());
                    pstmt.executeUpdate();
                }
            } else if (user instanceof Developer) {
                String insertDevSql = "INSERT INTO developers (user_id, company_name) VALUES (?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(insertDevSql)) {
                    pstmt.setString(1, user.getUserId());
                    pstmt.setString(2, ((Developer) user).getCompanyName());
                    pstmt.executeUpdate();
                }
            } else if (user instanceof Administrator) {
                String insertAdminSql = "INSERT INTO administrators (user_id, department) VALUES (?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(insertAdminSql)) {
                    pstmt.setString(1, user.getUserId());
                    pstmt.setString(2, ((Administrator) user).getDepartment());
                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("회원가입 중 데이터베이스 처리 오류가 발생했습니다.");
        }
    }
    
    @Override 
    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT user_id, password, name, user_type FROM users";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                UserEntity entity = fetchSubtypeEntity(
                    rs.getString("user_id"), rs.getString("password"), 
                    rs.getString("name"), rs.getString("user_type")
                );
                list.add(User.toDomain(entity, this));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    @Override 
    public void delete(String userId) {
        // DB.md에 설계된 ON DELETE CASCADE 덕분에 users만 지우면 하위 데이터도 싹 지워집니다!
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // 💡 Java 도메인의 API용 타입을 DB 스키마(대문자 스네이크 케이스)로 변환해주는 헬퍼
    private String mapToDBUserType(String javaType) {
        if ("MemberGamer".equals(javaType)) return "MEMBER_GAMER";
        if ("Developer".equals(javaType)) return "DEVELOPER";
        if ("Administrator".equals(javaType)) return "ADMINISTRATOR";
        return javaType.toUpperCase();
    }

    private UserEntity fetchSubtypeEntity(String userId, String password, String name, String userType) throws SQLException {
    if ("DEVELOPER".equals(userType)) {
        String devSql = "SELECT company_name FROM developers WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(devSql)) {
            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new DeveloperEntity(userId, password, name, rs.getString("company_name"));
                }
                throw new IllegalStateException("데이터 무결성 오류: developers 테이블에 데이터가 누락되었습니다. (user_id: " + userId + ")");
            }
        }
    } else if ("ADMINISTRATOR".equals(userType)) {
        String adminSql = "SELECT department FROM administrators WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(adminSql)) {
            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new AdministratorEntity(userId, password, name, rs.getString("department"));
                }
                throw new IllegalStateException("데이터 무결성 오류: administrators 테이블에 데이터가 누락되었습니다. (user_id: " + userId + ")");
            }
        }
    } else if ("MEMBER_GAMER".equals(userType)) {
        return new MemberGamerEntity(userId, password, name);
    }
    throw new IllegalArgumentException("알 수 없는 유저 타입: " + userType);
}
}