package dto;

public class DeveloperDTO extends UserDTO {
    private String companyName;

    public DeveloperDTO(String userId, String name, String role, String companyName) {
        super(userId, name, role);
        this.companyName = companyName;
    }

    public String getCompanyName() { return companyName; }

    @Override
    public String displayInfo() {
        return String.format("[개발자] ID: %s | 이름: %s | 소속: %s", 
                getUserId(), getName(), getCompanyName());
    }
}