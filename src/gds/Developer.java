package gds;
/*
 *
 * 1. DB 연동을 위한 가변 필드 전환
 * - company(소속 회사) 등의 정보가 변경될 수 있으므로 final 키워드 제거.
 * - updateCompany(String newCompany) 같은 의미 있는 데이터 수정 메서드 추가.
 * - User를 상속받아 공통 식별자(PK/UID)를 공유하는 구조 유지.
 */
public class Developer extends User {
    private final String companyName;

    public Developer(String userId, String password, String name, String companyName) {
        super(userId, password, name);
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    @Override
    public String getUserType() {
        return GConstant.UserType.DEVELOPER;
    }
}
