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
        this.userDAO.update(this);
    }

    @Override
    public String getUserType() {
        return GConstant.UserType.DEVELOPER;
    }

    @Override
    public UserDTO toDTO() {
        return new DeveloperDTO(this.userId, this.name, getUserType(), this.companyName);
    }
}