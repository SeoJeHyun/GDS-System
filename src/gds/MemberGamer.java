package gds;

import dao.UserDAO;
import dto.MemberGamerDTO;
import dto.UserDTO;

public class MemberGamer extends User {

    public MemberGamer(String userId, String name, UserDAO userDAO) {
        super(userId, name, userDAO);
        // 장바구니와 라이브러리는 과감히 제거되었습니다!
    }

    @Override
    public String getUserType() {
        return GConstant.UserType.MEMBER_GAMER;
    }

    @Override
    public UserDTO toDTO() {
        return new MemberGamerDTO(this.userId, this.name, getUserType());
    }
}