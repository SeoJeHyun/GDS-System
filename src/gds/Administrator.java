package gds;

import dao.UserDAO;
import dto.AdministratorDTO;
import dto.UserDTO;

public class Administrator extends User {
    private String department;

    public Administrator(String userId, String password, String name, String department, UserDAO userDAO) {
        super(userId, password, name, userDAO);
        this.department = department;
    }

    public String getDepartment() { return department; }

    public void updateDepartment(String newDepartment) {
        this.department = newDepartment;
        this.userDAO.update(this);
    }

    @Override
    public String getUserType() {
        return GConstant.UserType.ADMINISTRATOR;
    }

    @Override
    public UserDTO toDTO() {
        return new AdministratorDTO(this.userId, this.name, getUserType(), this.department);
    }
}