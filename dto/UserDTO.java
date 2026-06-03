package dto;

public abstract class UserDTO {
    private String userId;
    private String name;
    private String role; // 화면에 보여줄 권한 이름 (예: "Developer")

    public UserDTO(String userId, String name, String role) {
        this.userId = userId;
        this.name = name;
        this.role = role;
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getRole() { return role; }
    
    // UI 출력을 위한 공통 메서드
    public abstract String displayInfo(); 
}
