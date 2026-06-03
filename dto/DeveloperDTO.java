package dto;

public class DeveloperDTO extends UserDTO {
    private String companyName;

    public DeveloperDTO(String userId, String name, String userType, String companyName) {
        super(userId, name, userType);
        this.companyName = companyName;
    }

    public String getCompanyName() { return companyName; }
}