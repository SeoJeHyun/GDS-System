package dao;

import gds.User;
import java.util.List;

public interface UserDAO {
    // 1. 새로운 유저 저장 (회원가입 로직에 사용)
    void save(User user);

    // 2. ID를 통해 특정 유저 찾기 (로그인, 권한 확인에 사용)
    User findById(String userId);

    // 3. 모든 유저 목록 불러오기 (관리자용 시스템에 사용)
    List<User> findAll();

    // 4. 유저 정보 업데이트 (비밀번호 변경, 소속 부서 변경 등)
    void update(User user);

    // 5. 유저 삭제 (회원 탈퇴)
    void delete(String userId);
}