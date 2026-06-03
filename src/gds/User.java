package gds;

import dao.UserDAO; // 1단계에서 만든 인터페이스 임포트

public abstract class User {
    private final String userId;
    private String name;
    
    // 💡 유저님이 선택하신 '객체 내부 DAO 유지' (자식 클래스도 쓰도록 protected)
    protected final UserDAO userDAO; 

    // 💡 생성자를 통한 강제 주입
    public User(String userId, String name, UserDAO userDAO) {
        this.userId = userId;
        this.name = name;
        this.userDAO = userDAO;
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }

    // --- Active Record 스타일 비즈니스 로직 ---

    /**
     * [이름 변경 로직] 객체 상태를 바꾸고 즉시 DB에 동기화한다.
     */
    public void updateName(String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("이름은 공백일 수 없습니다.");
        }
        this.name = newName;
        // 💡 상태 변경 직후 객체가 스스로 창고지기(DAO)를 시켜 DB를 업데이트!
        this.userDAO.update(this); 
    }

    /**
     * [비밀번호 변경 로직] 메모리에 PW가 없으므로 DAO를 통해 DB에 직접 물어보고 바꾼다.
     */
    public void changePassword(String oldPassword, String newPassword) {
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("새로운 비밀번호는 공백일 수 없습니다.");
        }
        
        // 💡 (향후 UserDAO에 추가할 메서드) DB에 직접 기존 비밀번호가 맞는지 물어봄
        // if (!this.userDAO.verifyPassword(this.userId, oldPassword)) {
        //     throw new IllegalArgumentException("기존 비밀번호가 일치하지 않습니다.");
        // }
        
        // 💡 검증 통과 시 비밀번호만 따로 업데이트 해달라고 DAO에 요청
        // this.userDAO.updatePassword(this.userId, newPassword);
    }

    // --- static 로그인 팩토리 메서드 (3번 질문의 해결책) ---
    /*
    public static User login(String inputId, String inputPw, UserDAO dao) {
        // 1. dao를 이용해 DB에서 아이디/비밀번호 검증
        // 2. 일치하면 알맞은 User 자식 객체(MemberGamer 등)를 생성해서 반환
    }
    */

    public abstract String getUserType();
}