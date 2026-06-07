package service; // 또는 gds 패키지

import dao.UserDAO;
import dto.UserDTO;
import gds.MemberGamer;
import gds.User;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User login(String userId, String rawPassword) {
        // 1. DAO를 통해 유저를 불러옴
        User user = userDAO.findById(userId);

        // [풍부한 도메인 모델] Service는 도메인 객체의 내부를 꺼내보지 않고, 객체 스스로 검증하도록 위임합니다.
        if (user == null || !user.checkPassword(rawPassword)) {
            System.out.println("로그인 실패: 아이디 또는 비밀번호가 일치하지 않습니다.");
            return null;
        }

        return user;
    }

    // [API 4. 회원가입] 지휘 로직
    public User register(String userId, String rawPassword, String name) {
        // 1. 중복 아이디 검사
        if (userDAO.findById(userId) != null) {
            throw new IllegalStateException("이미 사용 중인 아이디입니다.");
        }

        // 2. 신규 게이머 객체 생성 (일반 회원가입은 MemberGamer로 고정)
        MemberGamer newUser = new MemberGamer(userId, rawPassword, name, userDAO);

        // 3. DB에 저장 지시
        userDAO.save(newUser);
        return newUser;
    }

    public UserDTO getUserInfo(User user) {
        return user.toDTO();
    }
}