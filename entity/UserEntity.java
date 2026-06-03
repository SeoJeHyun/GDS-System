package entity;

public abstract class UserEntity {
    private String userId;
    private String password;
    private String name;

    public UserEntity(String userId, String password, String name) {
        this.userId = userId;
        this.password = password;
        this.name = name;
    }

    public String getUserId() { return userId; }
    public String getPassword() { return password; }
    public String getName() { return name; }
}