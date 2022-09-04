package springbook.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * UserDao의 생성 책임을 맡은 팩토리 클래스
 */
@Configuration
public class DaoFactory {
    /**
     * UserDao를 생성
     *
     * @return UserDao 인스턴스
     */
    @Bean
    public UserDao userDao() {
        return new UserDao(this.connectionMaker());
    }
    
    /**
     * AccountDao를 생성
     *
     * @return AccountDao 인스턴스
     */
    @Bean
    public AccountDao accountDao() {
        return new AccountDao(this.connectionMaker());
    }
    
    /**
     * MessageDao를 생성
     *
     * @return MessageDao 인스턴스
     */
    @Bean
    public MessageDao messageDao() {
        return new MessageDao(this.connectionMaker());
    }
    
    /**
     * ConnectionMaker를 생성
     *
     * @return ConnectionMaker 인스턴스
     */
    @Bean
    public ConnectionMaker connectionMaker() {
        return new KConnectionMaker();
    }
}
