package gds;

import dao.UserDAO;
import dto.UserDTO;
import entity.DeveloperEntity;
import entity.UserEntity;

public abstract class User {
    protected final String userId;
    protected String name;
    protected final UserDAO userDAO;

    public User(String userId, String name, UserDAO userDAO) {
        this.userId = userId;
        this.name = name;
        this.userDAO = userDAO;
    }

    public String getUserId() { return userId; }
    public String getName() { return name; }
    public abstract String getUserType();

    // 1. 도메인 비즈니스 로직 (상태를 바꾸고 스스로 DB 업데이트)
    public void updateName(String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("이름은 공백일 수 없습니다.");
        }
        this.name = newName;
        this.userDAO.update(this); // 객체 스스로 영속화!
    }

    // 2. 화면으로 나갈 때 쓸 DTO 변환 로직 (자식들이 각자 알맞은 DTO를 만들도록 강제)
    public abstract UserDTO toDTO();

    // 3. DB에서 읽어온 Entity를 진정한 도메인 객체로 조립해주는 팩토리 메서드
    public static User fromEntity(UserEntity entity, UserDAO dao) {
        if (entity instanceof DeveloperEntity) {
            DeveloperEntity devEntity = (DeveloperEntity) entity;
            // 도메인 생성 시 비밀번호는 버리고, DAO를 쥐여줌
            return new Developer(devEntity.getUserId(), devEntity.getName(), devEntity.getCompanyName(), dao);
        }
        
        // TODO: AdministratorEntity와 MemberGamerEntity에 대한 분기 처리 추가
        
        throw new IllegalArgumentException("알 수 없는 유저 엔티티 타입입니다.");
    }
}