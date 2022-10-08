package springbook.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import springbook.user.dao.DaoFactory;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
    
    List<User> users; //테스트 픽스처
    
    @BeforeEach
    public void setUp() {
        users = List.of(
                new User("bumjin", "박범진", "p1", Level.BASIC, 49, 0),
                new User("joytouch", "강명성", "p2", Level.BASIC, 50, 0),
                new User("erwins", "신승한", "p3", Level.SILVER, 60, 29),
                new User("madnite1", "이상호", "p4", Level.SILVER, 60, 30),
                new User("green", "오민규", "p5", Level.GOLD, 100, 100)
        );
    }
    
    @DisplayName("빈 주입 테스트")
    @Test
    public void bean() {
        assertThat(this.userService).isNotNull();
    }
    
    @DisplayName("사용자 레벨 업그레이드 테스트")
    @Test
    public void upgradeLevels() {
        // 데이터 초기화
        this.userDao.deleteAll();
        
        // Given
        for (final User user : users) {
            this.userDao.add(user);
        }
        
        // When
        this.userService.upgradeLevels();
        
        // Then
        this.checkLevel(users.get(0), Level.BASIC);
        this.checkLevel(users.get(1), Level.SILVER);
        this.checkLevel(users.get(2), Level.SILVER);
        this.checkLevel(users.get(3), Level.GOLD);
        this.checkLevel(users.get(4), Level.GOLD);
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
                user1.recommend()
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
    
    private void checkLevel(final User user, final Level expectedLevel) {
        final User userUpdate = this.userDao.get(user.id());
        
        assertThat(userUpdate.level()).isEqualTo(expectedLevel);
    }
}