package gds;

public class NonMemberGamer extends User {
    public NonMemberGamer() {
        super(
                GConstant.Guest.USER_ID,
                GConstant.Guest.PASSWORD,
                GConstant.Guest.NAME
        );
    }

    @Override
    public String getUserType() {
        return GConstant.UserType.NON_MEMBER_GAMER;
    }
}
