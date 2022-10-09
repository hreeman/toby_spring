package springbook.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import springbook.user.dao.DaoFactory;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;
import springbook.user.policy.NormallyUserLevelUpgradePolicy;
import springbook.user.policy.UserLevelUpgradePolicy;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * User Service 테스트
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DaoFactory.class)
class UserServiceTest {
    @Autowired
    UserDao userDao;
    
    @Autowired
    UserService userService;
    
    @Autowired
    UserServiceImpl userServiceImpl;
    
    @Autowired
    UserLevelUpgradePolicy userLevelUpgradePolicy;
    
    @Autowired
    PlatformTransactionManager transactionManager;
    
    List<User> users; //테스트 픽스처
    
    @BeforeEach
    public void setUp() {
        users = List.of(
                new User("bumjin", "박범진", "p1", Level.BASIC, this.userLevelUpgradePolicy.minLogcountForSilver() - 1, 0, "test1@mail.com"),
                new User("joytouch", "강명성", "p2", Level.BASIC, this.userLevelUpgradePolicy.minLogcountForSilver(), 0, "test2@mail.com"),
                new User("erwins", "신승한", "p3", Level.SILVER, 60, this.userLevelUpgradePolicy.minRecommendForGold() - 1, "test3@mail.com"),
                new User("madnite1", "이상호", "p4", Level.SILVER, 60, this.userLevelUpgradePolicy.minRecommendForGold(), "test4@mail.com"),
                new User("green", "오민규", "p5", Level.GOLD, 100, Integer.MAX_VALUE, "test5@mail.com")
        );
    }
    
    @DisplayName("빈 주입 테스트")
    @Test
    public void bean() {
        assertThat(this.userService).isNotNull();
    }
    
    @DisplayName("사용자 레벨 업그레이드 테스트")
    @Test
    @DirtiesContext // 컨텍스트의 DI 설정을 변경(더럽힌다)하는 테스트라는것을 알려줌
    public void upgradeLevels() throws Exception {
        // 데이터 초기화
        this.userDao.deleteAll();
        
        // Given
        for (final User user : users) {
            this.userDao.add(user);
        }
        
        final MockMailSender mockMailSender = new MockMailSender();
        this.userServiceImpl.setMailSender(mockMailSender);
        
        // When
        this.userService.upgradeLevels();
        
        // Then
        this.checkLevelUpgraded(users.get(0), false);
        this.checkLevelUpgraded(users.get(1), true);
        this.checkLevelUpgraded(users.get(2), false);
        this.checkLevelUpgraded(users.get(3), true);
        this.checkLevelUpgraded(users.get(4), false);
        
        final List<String> requests = mockMailSender.getRequests();
        
        assertThat(requests.size()).isEqualTo(2);
        assertThat(requests.get(0)).isEqualTo(users.get(1).email());
        assertThat(requests.get(1)).isEqualTo(users.get(3).email());
    }
    
    @DisplayName("add 메서드시 등급 데이터 테스트")
    @Test
    public void add() {
        // 데이터 초기화
        this.userDao.deleteAll();
        
        // Given
        final User userWithLevel = users.get(4); //GOLD 레벨이 이미 지정된 User라면 레벨을 초기화 하지 않음
        
        // 레벨이 비어있는 사용자, 로직에 따라 등록 중 BASIC 레벨이 설정되어야 함
        final User user1 = users.get(0);
        final User userWithoutLevel = new User(
                user1.id(),
                user1.name(),
                user1.password(),
                null,
                user1.login(),
                user1.recommend(),
                user1.email()
        );
        
        // When
        this.userService.add(userWithLevel);
        this.userService.add(userWithoutLevel);
        
        // Then
        final User userWithLevelRead = this.userDao.get(userWithLevel.id());
        final User userWithoutLevelRead = this.userDao.get(userWithoutLevel.id());
        
        assertThat(userWithLevelRead.level()).isEqualTo(userWithLevel.level());
        assertThat(userWithoutLevelRead.level()).isEqualTo(Level.BASIC);
    }
    
    @DisplayName("예외 발생시 작업 취소 여부 테스트")
    @Test
    public void upgradeAllOrNothing() throws Exception {
        //수동으로 DI
        final UserServiceImpl userServiceImpl = new UserServiceImpl();
        userServiceImpl.setUserDao(this.userDao);
        userServiceImpl.setMailSender(new DummyMailSender());
        userServiceImpl.setUserLevelUpgradePolicy(new TestUserLevelUpgradePolicy(users.get(3).id()));
        
        final UserServiceTx testUserService = new UserServiceTx();
        testUserService.setUserService(userServiceImpl);
        testUserService.setTransactionManager(this.transactionManager);
        
        // 데이터 초기화
        this.userDao.deleteAll();
        
        for (final User user : users) {
            userDao.add(user);
        }
        
        try {
            testUserService.upgradeLevels();
            fail("TestUserLevelUpgradePolicyException expected");
        } catch (final TestUserLevelUpgradePolicyException e) {
        
        }
        
        checkLevelUpgraded(users.get(1), false);
    }
    
    @Deprecated
    private void checkLevel(final User user, final Level expectedLevel) {
        final User userUpdate = this.userDao.get(user.id());
        
        assertThat(userUpdate.level()).isEqualTo(expectedLevel);
    }
    
    private void checkLevelUpgraded(final User user, final boolean upgraded) {
        final User userUpdate = this.userDao.get(user.id());
        
        if (upgraded) {
            assertThat(userUpdate.level()).isEqualTo(user.level().nextLevel());
        } else {
            assertThat(userUpdate.level()).isEqualTo(user.level());
        }
    }

    /**
     * 레벨 업그레이드 정책을 테스트하기 위한 대역 클래스
     */
    static class TestUserLevelUpgradePolicy extends NormallyUserLevelUpgradePolicy {
        private String id;
    
        //예외를 발생시킬 User 오브젝트의 id 지정
        public TestUserLevelUpgradePolicy(final String id) {
            this.id = id;
        }
    
        @Override
        public User upgradeLevel(final User user) {
            if (user.id().equals(this.id)) {
                throw new TestUserLevelUpgradePolicyException();
            }
            
            return super.upgradeLevel(user);
        }
    }
    
    static class TestUserLevelUpgradePolicyException extends RuntimeException {
    
    }
    
    static class MockMailSender implements MailSender {
        private List<String> requests = new ArrayList<>();
    
        public List<String> getRequests() {
            return requests;
        }
    
        @Override
        public void send(final SimpleMailMessage mailMessage) throws MailException {
            requests.add(mailMessage.getTo()[0]);
        }
    
        @Override
        public void send(final SimpleMailMessage... simpleMessages) throws MailException {
        
        }
    }
}