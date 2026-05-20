package dto;

public class UserResponseDTO {
    private String userId;
    // 비밀번호 필드는 절대 여기에 포함시키지 않습니다!
    private String role; // 예: "일반 게이머", "개발자", "관리자"

    public UserResponseDTO(String userId, String role) {
        this.userId = userId;
        this.role = role;
    }

    public String getUserId() { return userId; }
    public String getRole() { return role; }

    @Override
    public String toString() {
        return String.format("[로그인됨] ID: %s | 권한: %s", userId, role);
    }
}
