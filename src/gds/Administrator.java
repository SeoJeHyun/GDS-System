package gds;

/*
1. 가변 필드 허용
- department 필드의 final 키워드 제거(부서 이동 반영이 가능하도록 하기 위함)
- updateDepartment 등의 수정 메서드 추가

2. 타입 안정성 및 DB 저장 최적화 (Enum 도입)
- GConstant 내부의 UserType String 상수들을 독립적인 enum 클래스로 분리할 것
- 자바 코드의 가독성을 챙기고, 실제 DB에 저장할 때는 용량 최적화를 위해 "NG", "MG", "DP", "AD" 등의
 두자리 코드로 매핑되도록 처리
*/ 
public class Administrator extends User {
    private final String department;

    public Administrator(String userId, String password, String name, String department) {
        super(userId, password, name);
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    @Override
    public String getUserType() {
        return GConstant.UserType.ADMINISTRATOR;
    }
}
