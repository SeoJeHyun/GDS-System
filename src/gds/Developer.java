package gds;

public class Developer extends User {
    private final String companyName;

    public Developer(String userId, String password, String name, String companyName) {
        super(userId, password, name);
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    @Override
    public String getUserType() {
        return GConstant.UserType.DEVELOPER;
    }
}
