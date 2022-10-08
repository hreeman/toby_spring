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
    
    private void checkLevel(final User user, final Level expectedLevel) {
        final User userUpdate = this.userDao.get(user.id());
        
        assertThat(userUpdate.level()).isEqualTo(expectedLevel);
    }
}