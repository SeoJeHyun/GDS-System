package dto;

public abstract class UserDTO {
    private String userId;
    private String name;
    private String userType;

    public UserDTO(String userId, String name, String userType) {
        this.userId = userId;
        this.name = name;
        this.userType = userType;
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getUserType() { return userType; }
}