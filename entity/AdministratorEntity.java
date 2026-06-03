package entity;

public class AdministratorEntity extends UserEntity {
    private String department;

    public AdministratorEntity(String userId, String password, String name, String department) {
        super(userId, password, name);
        this.department = department;
    }

    public String getDepartment() { return department; }
}