package springbook.user.domain;

public class User {
    private String id;
    private String name;
    private String password;
    private Level level;
    private int login;
    private int recommend;
    private String email;
    
    public User(final String id, final String name, final String password, final Level level, final int login, final int recommend, final String email) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.level = level;
        this.login = login;
        this.recommend = recommend;
        this.email = email;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(final String password) {
        this.password = password;
    }
    
    public Level getLevel() {
        return level;
    }
    
    public void setLevel(final Level level) {
        this.level = level;
    }
    
    public int getLogin() {
        return login;
    }
    
    public void setLogin(final int login) {
        this.login = login;
    }
    
    public int getRecommend() {
        return recommend;
    }
    
    public void setRecommend(final int recommend) {
        this.recommend = recommend;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(final String email) {
        this.email = email;
    }
    
    
    /**
     * 레벨 업그레이드 요청
     */
    public void upgradeLevel() {
        final Level nextLevel = this.level.nextLevel();
        
        if (nextLevel == null) {
            throw new IllegalStateException(this.level + "은 업그레이드가 불가능 합니다.");
        }
        
        this.level = nextLevel;
    }
}
