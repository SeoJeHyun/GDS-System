package dto;

public class ApiResponseDTO<T> {
    private String status;
    private String message;
    private T data;

    public ApiResponseDTO(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // 성공 응답을 쉽게 만들기 위한 팩토리 메서드
    public static <T> ApiResponseDTO<T> success(String message, T data) {
        return new ApiResponseDTO<>("success", message, data);
    }

    // 실패 응답을 쉽게 만들기 위한 팩토리 메서드
    public static <T> ApiResponseDTO<T> fail(String message) {
        return new ApiResponseDTO<>("fail", message, null);
    }
    
    // Getter (JSON 변환 라이브러리(예: Jackson, Gson)가 읽어갈 때 필수)
    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public T getData() { return data; }
}