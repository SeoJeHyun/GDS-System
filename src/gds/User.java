package gds;

//Developer, NonMemberGamer, MemberGamer, Administrator의 부모 클래스
//생성시에 userId, password, name을 저장

/*
1. 식별자 분리( 보안 및 DB 연동)
- 사용자가 입력하는 '로그인용 ID'와 시스템 내부에서 쓰는 '고유 PK(UID)'를 엄격히 분리할 것
- DB 연동을 위해 불변하는 재부 식별자(ex. Long id, UUID) 필드의 추가 필요

2. 가변 필드 전환(데이터 수정 허용) 
- DB 데이터 업데이트를 반영할 수 있도록 password, name 필드의 final 제거

3. 비밀번호 보안 강화
- checkPassword() 로직을 강화해야 함

4. 세션/토큰 기반 인증 준비
- 향후 클라이언트 서버 간 코드 분리 후 임의의 UID 조작을 방지하고자 로그인 성공 시
session ID 또는 JWT 검증 과정을 실시
*/
public abstract class User {
    private final String userId;
    private final String password;
    private final String name;

    public User(String userId, String password, String name) {
        this.userId = userId;
        this.password = password;
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public abstract String getUserType();
}
