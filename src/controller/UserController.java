package controller;

import dto.ApiResponseDTO;
import dto.LoginRequestDTO;
import dto.RegisterRequestDTO;
import dto.UserDTO;
import dto.GameResponseDTO;
import gds.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.LibraryService;
import service.UserService;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;
    private final LibraryService libraryService;

    public UserController(UserService userService, LibraryService libraryService) {
        this.userService = userService;
        this.libraryService = libraryService;
    }

    // [API 3. 로그인]
    // 프론트엔드로부터 JSON -> LoginRequestDTO 변환되어 들어왔다고 가정합니다.
    @PostMapping("/api/login")
    public ApiResponseDTO<UserDTO> login(@RequestBody LoginRequestDTO request) {
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
    @PostMapping("/api/register")
    public ApiResponseDTO<UserDTO> register(@RequestBody RegisterRequestDTO request) {
        try {
            // 주방장에게 회원가입 지시
            User newUser = userService.register(request.getUserId(), request.getPassword(), request.getName());
            return ApiResponseDTO.success("회원가입 성공", newUser.toDTO());
            
        } catch (IllegalStateException e) {
            return ApiResponseDTO.fail(e.getMessage()); // "이미 사용 중인 아이디입니다." 등 반환
        }
    }

    // [API 8. 라이브러리 조회]
    @GetMapping("/api/users/{userId}/library")
    public ApiResponseDTO<List<GameResponseDTO>> getLibrary(@PathVariable String userId, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "100") int size) {
        try {
            List<GameResponseDTO> libraryGames = libraryService.getUserLibrary(userId);
            return ApiResponseDTO.success("라이브러리 조회 성공", libraryGames);
        } catch (Exception e) {
            return ApiResponseDTO.fail(e.getMessage());
        }
    }

    // [API 7. 게임 파일 다운로드]
    @GetMapping("/api/users/{userId}/library/{gameId}/download")
    public ResponseEntity<String> downloadGame(@PathVariable String userId, @PathVariable String gameId) {
        try {
            String fileContent = libraryService.getGameDownloadContent(userId, gameId);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + gameId + ".txt\"")
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(fileContent);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
