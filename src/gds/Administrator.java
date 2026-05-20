package gds;

public class Administrator extends User {
    private final String department;

    public Administrator(String userId, String password, String name, String department) {
        super(userId, password, name);
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    @Override
    public String getUserType() {
        return GConstant.UserType.ADMINISTRATOR;
    }
}
