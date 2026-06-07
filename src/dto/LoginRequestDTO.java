package dto;

public class LoginRequestDTO {
    private String userId;
    private String password;

    public LoginRequestDTO() {}

    public LoginRequestDTO(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public String getUserId() { return userId; }
    public String getPassword() { return password; }
}