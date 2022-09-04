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
        final ConnectionMaker connectionMaker = new KConnectionMaker();
    
        return new UserDao(connectionMaker);
    }
    
    /**
     * AccountDao를 생성
     *
     * @return AccountDao 인스턴스
     */
    public AccountDao accountDao() {
        final ConnectionMaker connectionMaker = new KConnectionMaker();
        
        return new AccountDao(connectionMaker);
    }
    
    /**
     * MessageDao를 생성
     *
     * @return MessageDao 인스턴스
     */
    public MessageDao messageDao() {
        final ConnectionMaker connectionMaker = new KConnectionMaker();
        
        return new MessageDao(connectionMaker);
    }
}
