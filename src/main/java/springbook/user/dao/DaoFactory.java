package springbook.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import springbook.user.policy.NormallyUserLevelUpgradePolicy;
import springbook.user.policy.UserLevelUpgradePolicy;
import springbook.user.service.DummyMailSender;
import springbook.user.service.UserService;

import javax.sql.DataSource;

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
        final UserDaoJdbc userDao = new UserDaoJdbc();
    
        userDao.setDataSource(this.dataSource());
    
        return userDao;
    }
    
    /**
     * AccountDao를 생성
     *
     * @return AccountDao 인스턴스
     */
    @Bean
    public AccountDao accountDao() {
        final AccountDao accountDao = new AccountDao();
        
        accountDao.setDataSource(this.dataSource());
        
        return accountDao;
    }
    
    /**
     * MessageDao를 생성
     *
     * @return MessageDao 인스턴스
     */
    @Bean
    public MessageDao messageDao() {
        final MessageDao messageDao = new MessageDao();
        
        messageDao.setDataSource(this.dataSource());
        
        return messageDao;
    }
    
    /**
     * DataSource 생성
     *
     * @return DataSource 인스턴스
     */
    @Bean
    public DataSource dataSource() {
        final SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        
        dataSource.setDriverClass(org.mariadb.jdbc.Driver.class);
        dataSource.setUrl("jdbc:mariadb://localhost:3306/toby_spring");
        dataSource.setUsername("toby");
        dataSource.setPassword("toby");
        
        return dataSource;
    }
    
    /**
     * 사용자 레벨 업그레이드 정책
     *
     * @return 사용자 레벨 업그레이드 정책 인스턴스
     */
    @Bean
    public UserLevelUpgradePolicy userLevelUpgradePolicy() {
        return new NormallyUserLevelUpgradePolicy();
    }
    
    /**
     * 추상화된 트랜잭션 매니저
     *
     * @return 트랜잭션 매니저 인스턴스
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(this.dataSource());
    }
    
    /**
     * 이메일 발송 구현체
     *
     * @return JavaX의 mail을 추상화한 스프링의 MailSender 인스턴스
     */
    @Bean
    public MailSender mailSender() {
        final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        
        mailSender.setHost("mail.server.com");
    
        return mailSender;
    }
    
    /**
     * 테스트용 테스트 스텁 메일 발송 구현체
     *
     * @return 테스트용 MailSender 인스턴스
     */
    @Bean
    public MailSender dummyMailSender() {
        return new DummyMailSender();
    }
    
    /**
     * User Service 생성
     *
     * @return UserService 인스턴스
     */
    @Bean
    public UserService userService() {
        final UserService userService = new UserService();
        
        userService.setUserDao(this.userDao());
        userService.setUserLevelUpgradePolicy(this.userLevelUpgradePolicy());
        userService.setTransactionManager(this.transactionManager());
        userService.setMailSender(this.dummyMailSender());
        
        return userService;
    }
}
