package controller;

import dto.ApiResponseDTO;
import dto.LoginRequestDTO;
import dto.RegisterRequestDTO;
import dto.UserDTO;
import gds.User;
import service.UserService;

public class UserController {
    private final UserService userService;

    // 문지기는 주방장(Service)을 주입받아 사용합니다.
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // [API 3. 로그인]
    // 프론트엔드로부터 JSON -> LoginRequestDTO 변환되어 들어왔다고 가정합니다.
    public ApiResponseDTO<UserDTO> login(LoginRequestDTO request) {
        try {
            // 주방장에게 로그인 검증 지시
            User user = userService.login(request.getUserId(), request.getPassword());
            
            if (user == null) {
                return ApiResponseDTO.fail("아이디 또는 비밀번호가 일치하지 않습니다.");
            }
            
            // 성공 시 명세서에 맞게 포장하여 반환 (세션/토큰은 일단 생략하고 UserDTO만 담음)
            return ApiResponseDTO.success("로그인 성공", user.toDTO());
            
        } catch (Exception e) {
            return ApiResponseDTO.fail(e.getMessage());
        }
    }

    // [API 4. 회원가입]
    public ApiResponseDTO<UserDTO> register(RegisterRequestDTO request) {
        try {
            // 주방장에게 회원가입 지시
            User newUser = userService.register(request.getUserId(), request.getPassword(), request.getName());
            return ApiResponseDTO.success("회원가입 성공", newUser.toDTO());
            
        } catch (IllegalStateException e) {
            return ApiResponseDTO.fail(e.getMessage()); // "이미 사용 중인 아이디입니다." 등 반환
        }
    }
}
