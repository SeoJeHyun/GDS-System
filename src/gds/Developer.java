package gds;

import dao.UserDAO;
import dto.DeveloperDTO;
import dto.UserDTO;

public class Developer extends User {
    private String companyName;

    public Developer(String userId, String name, String companyName, UserDAO userDAO) {
        super(userId, name, userDAO);
        this.companyName = companyName;
    }

    public String getCompanyName() { return companyName; }

    public void updateCompany(String newCompanyName) {
        this.companyName = newCompanyName;
        this.userDAO.update(this); // 소속 회사가 바뀌면 스스로 업데이트
    }

    @Override
    public String getUserType() {
        return GConstant.UserType.DEVELOPER;
    }

    @Override
    public UserDTO toDTO() {
        // 자신의 정보를 모아 안전한 DeveloperDTO로 변환하여 반환
        return new DeveloperDTO(this.userId, this.name, getUserType(), this.companyName);
    }
}