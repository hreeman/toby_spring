package springbook.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import springbook.learningtest.jdk.TransactionHandler;
import springbook.user.dao.DaoFactory;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;
import springbook.user.policy.NormallyUserLevelUpgradePolicy;
import springbook.user.policy.UserLevelUpgradePolicy;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * User Service 테스트
 */
@ExtendWith({SpringExtension.class, MockitoExtension.class})
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
    void bean() {
        assertThat(this.userService).isNotNull();
    }
    
    @DisplayName("사용자 레벨 업그레이드 테스트")
    @Test
    void upgradeLevels() {
        // Given
        final MockUserDao mockUserDao = new MockUserDao(this.users);
        
        final UserServiceImpl userServiceImpl = new UserServiceImpl();
        final MockMailSender mockMailSender = new MockMailSender();
        
        userServiceImpl.setUserDao(mockUserDao);
        userServiceImpl.setMailSender(mockMailSender);
        userServiceImpl.setUserLevelUpgradePolicy(this.userLevelUpgradePolicy);
        
        // When
        userServiceImpl.upgradeLevels();
        
        // Then
        final List<User> updated = mockUserDao.getUpdated();
        assertThat(updated).hasSize(2);
        this.checkUserAndLevel(updated.get(0), "joytouch", Level.SILVER);
        this.checkUserAndLevel(updated.get(1), "madnite1", Level.GOLD);
        
        final List<String> requests = mockMailSender.getRequests();
        
        assertThat(requests).hasSize(2);
        assertThat(requests.get(0)).isEqualTo(this.users.get(1).getEmail());
        assertThat(requests.get(1)).isEqualTo(this.users.get(3).getEmail());
    }
    
    private void checkUserAndLevel(final User updated, final String expectedId, final Level expectedLevel) {
        assertThat(updated.getId()).isEqualTo(expectedId);
        assertThat(updated.getLevel()).isEqualTo(expectedLevel);
    }
    
    @DisplayName("add 메서드시 등급 데이터 테스트")
    @Test
    void add() {
        // 데이터 초기화
        this.userDao.deleteAll();
        
        // Given
        final User userWithLevel = this.users.get(4); //GOLD 레벨이 이미 지정된 User라면 레벨을 초기화 하지 않음
        
        // 레벨이 비어있는 사용자, 로직에 따라 등록 중 BASIC 레벨이 설정되어야 함
        final User user1 = this.users.get(0);
        final User userWithoutLevel = new User(
                user1.getId(),
                user1.getName(),
                user1.getPassword(),
                null,
                user1.getLogin(),
                user1.getRecommend(),
                user1.getEmail()
        );
        
        // When
        this.userService.add(userWithLevel);
        this.userService.add(userWithoutLevel);
        
        // Then
        final User userWithLevelRead = this.userDao.get(userWithLevel.getId());
        final User userWithoutLevelRead = this.userDao.get(userWithoutLevel.getId());
        
        assertThat(userWithLevelRead.getLevel()).isEqualTo(userWithLevel.getLevel());
        assertThat(userWithoutLevelRead.getLevel()).isEqualTo(Level.BASIC);
    }
    
    @DisplayName("예외 발생시 작업 취소 여부 테스트")
    @Test
    void upgradeAllOrNothing() {
        //수동으로 DI
        final UserServiceImpl userServiceImpl = new UserServiceImpl();
        userServiceImpl.setUserDao(this.userDao);
        userServiceImpl.setMailSender(new DummyMailSender());
        userServiceImpl.setUserLevelUpgradePolicy(new TestUserLevelUpgradePolicy(this.users.get(3).getId()));
        
        final TransactionHandler txHandler = new TransactionHandler();
        
        txHandler.setTarget(userServiceImpl);
        txHandler.setTransactionManager(this.transactionManager);
        txHandler.setPattern("upgradeLevels");
        
        final UserService testUserService = (UserService) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{ UserService.class },
                txHandler
        );
        
        // 데이터 초기화
        this.userDao.deleteAll();
        
        this.users.forEach(this.userDao::add);
        
        try {
            testUserService.upgradeLevels();
            fail("TestUserLevelUpgradePolicyException expected");
        } catch (final TestUserLevelUpgradePolicyException e) {
        
        }
        
        this.checkLevelUpgraded(this.users.get(1), false);
    }
    
    @DisplayName("Mockito를 적용한 테스트 코드")
    @Test
    public void mockUpgradeLevels() {
        final UserServiceImpl userServiceImpl = new UserServiceImpl();
        
        final UserDao mockUserDao = mock(UserDao.class);
        
        when(mockUserDao.getAll()).thenReturn(this.users);
        userServiceImpl.setUserDao(mockUserDao);
        userServiceImpl.setUserLevelUpgradePolicy(new NormallyUserLevelUpgradePolicy());
        
        final MailSender mockMailSender = mock(MailSender.class);
        userServiceImpl.setMailSender(mockMailSender);
        
        userServiceImpl.upgradeLevels();
        
        verify(mockUserDao, times(2)).update(any(User.class));
        
        verify(mockUserDao).update(this.users.get(1));
        assertThat(this.users.get(1).getLevel()).isEqualTo(Level.SILVER);
        
        verify(mockUserDao).update(this.users.get(3));
        assertThat(this.users.get(3).getLevel()).isEqualTo(Level.GOLD);
        
        
        final ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class);
        
        verify(mockMailSender, times(2)).send(mailMessageArg.capture());
        
        final List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
        
        assertThat(mailMessages.get(0).getTo()[0]).isEqualTo(this.users.get(1).getEmail());
        assertThat(mailMessages.get(1).getTo()[0]).isEqualTo(this.users.get(3).getEmail());
    }

    private void checkLevelUpgraded(final User user, final boolean upgraded) {
        final User userUpdate = this.userDao.get(user.getId());
        
        if (upgraded) {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel().nextLevel());
        } else {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel());
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
        public void upgradeLevel(final User user) {
            if (user.getId().equals(this.id)) {
                throw new TestUserLevelUpgradePolicyException();
            }
            
            super.upgradeLevel(user);
        }
    }
    
    static class TestUserLevelUpgradePolicyException extends RuntimeException {
    
    }
    
    static class MockMailSender implements MailSender {
        private List<String> requests = new ArrayList<>();
    
        public List<String> getRequests() {
            return this.requests;
        }
    
        @Override
        public void send(final SimpleMailMessage mailMessage) throws MailException {
            this.requests.add(Objects.requireNonNull(mailMessage.getTo())[0]);
        }
    
        @Override
        public void send(final SimpleMailMessage... simpleMessages) throws MailException {
        
        }
    }
    
    static class MockUserDao implements UserDao {
        private List<User> users;
        private List<User> updated = new ArrayList<>();
    
        public MockUserDao(final List<User> users) {
            this.users = users;
        }
    
        public List<User> getUpdated() {
            return this.updated;
        }
    
        @Override
        public void update(final User updateUser) {
            this.updated.add(updateUser);
        }
    
        @Override
        public List<User> getAll() {
            return this.users;
        }
    
        @Override
        public void add(final User user) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public User get(final String id) {
            throw new UnsupportedOperationException();
        }
    
        @Override
        public void deleteAll() {
            throw new UnsupportedOperationException();
        }
    
        @Override
        public int getCount() {
            throw new UnsupportedOperationException();
        }
    }
}