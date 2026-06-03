package dto;

public class AdministratorDTO extends UserDTO {
    private String department;

    public AdministratorDTO(String userId, String name, String userType, String department) {
        super(userId, name, userType);
        this.department = department;
    }

    public String getDepartment() { return department; }
}