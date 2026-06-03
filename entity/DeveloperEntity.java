package entity;

public class DeveloperEntity extends UserEntity {
    private String companyName; // 고유 데이터

    public DeveloperEntity(String userId, String password, String name, String companyName) {
        super(userId, password, name);
        this.companyName = companyName;
    }

    public String getCompanyName() { return companyName; }
}