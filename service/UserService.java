package service; // 또는 gds 패키지

import dao.UserDAO;
import dto.UserDTO;
import gds.User;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User login(String userId, String rawPassword) {
        // 1. DAO를 통해 유저를 불러옴
        User user = userDAO.findById(userId);

        // 2. 💡 검증 로직이 드디어 Service로 완전히 넘어왔습니다!
        // (향후 이곳에 BCrypt.checkpw(rawPassword, user.getPassword()) 같은 암호화 검증이 들어갈 자리입니다)
        if (user == null || !user.getPassword().equals(rawPassword)) {
            System.out.println("로그인 실패: 아이디 또는 비밀번호가 일치하지 않습니다.");
            return null;
        }

        return user;
    }

    public UserDTO getUserInfo(User user) {
        return user.toDTO();
    }
}