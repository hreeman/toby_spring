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
}
