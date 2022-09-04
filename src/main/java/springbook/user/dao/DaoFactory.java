package springbook.user.dao;

/**
 * UserDao의 생성 책임을 맡은 팩토리 클래스
 */
public class DaoFactory {
    /**
     * UserDao를 생성
     *
     * @return UserDao 인스턴스
     */
    public UserDao userDao() {
        return new UserDao(this.connectionMaker());
    }
    
    /**
     * AccountDao를 생성
     *
     * @return AccountDao 인스턴스
     */
    public AccountDao accountDao() {
        return new AccountDao(this.connectionMaker());
    }
    
    /**
     * MessageDao를 생성
     *
     * @return MessageDao 인스턴스
     */
    public MessageDao messageDao() {
        return new MessageDao(this.connectionMaker());
    }
    
    /**
     * ConnectionMaker를 생성
     *
     * @return ConnectionMaker 인스턴스
     */
    public ConnectionMaker connectionMaker() {
        return new KConnectionMaker();
    }
}
