package gds;

import dao.UserDAO;
import dto.UserDTO;
import entity.AdministratorEntity;
import entity.DeveloperEntity;
import entity.MemberGamerEntity;
import entity.UserEntity;

public abstract class User {
    protected final String userId;
    protected String name;
    protected final UserDAO userDAO;
    protected String password;

    public User(String userId, String password, String name, UserDAO userDAO) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.userDAO = userDAO;
    }

    // [캡슐화] 스스로 비밀번호를 검증하는 지능형 메서드
    public boolean checkPassword(String rawPassword) {
        // 💡 유저님의 보안 인사이트 반영!
        // 실제 운영 환경에서 this.password는 DB에서 꺼내온 '해싱된 암호문'입니다.
        // 향후 이곳은 평문 비교(equals)가 아니라 해시 검증 로직(예: BCrypt.checkpw(rawPassword, this.password))으로 교체되어야 합니다.
        return this.password != null && this.password.equals(rawPassword); 
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public abstract String getUserType();

    // 정보 업데이트 비즈니스 로직 (스스로 DB 동기화)
    public void updateName(String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("이름은 공백일 수 없습니다.");
        }
        this.name = newName;
        this.userDAO.update(this); 
    }

    // 밖으로 나갈 때 사용할 DTO 포장 지시
    public abstract UserDTO toDTO();

    // 💡 선택해주신 'toDomain' 네이밍 적용! (조립 공장)
    public static User toDomain(UserEntity entity, UserDAO dao) {
        if (entity instanceof DeveloperEntity) {
            DeveloperEntity devEntity = (DeveloperEntity) entity;
            return new Developer(devEntity.getUserId(), devEntity.getPassword(), devEntity.getName(), devEntity.getCompanyName(), dao);
        } else if (entity instanceof AdministratorEntity) {
            AdministratorEntity adminEntity = (AdministratorEntity) entity;
            return new Administrator(adminEntity.getUserId(), adminEntity.getPassword(), adminEntity.getName(), adminEntity.getDepartment(), dao);
        } else if (entity instanceof MemberGamerEntity) {
            MemberGamerEntity gamerEntity = (MemberGamerEntity) entity;
            return new MemberGamer(gamerEntity.getUserId(), gamerEntity.getPassword(), gamerEntity.getName(), dao);
        }
        
        throw new IllegalArgumentException("알 수 없는 유저 엔티티 타입입니다.");
    }
}