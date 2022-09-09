package springbook.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import springbook.user.dao.DaoFactory;
import springbook.user.dao.UserDao;
import springbook.user.domain.User;

import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 테스트를 위한 main 메서드를 실행 시키기 위한 클래스
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DaoFactory.class)
public class UserDaoTest {
    @Autowired
    private ApplicationContext context;
    
    private UserDao userDao;
    private User user1;
    private User user2;
    private User user3;
    
    @BeforeEach
    public void setUp() {
        this.userDao = this.context.getBean("userDao", UserDao.class);
        
        this.user1 = new User("toby", "토비", "toby3");
        this.user2 = new User("kimyh", "김영한", "kim1234");
        this.user3 = new User("whiteship", "백기선", "white1234");
    }
    
    @DisplayName("데이터 DB에 등록 후 조회한 결과와 등록한 결과가 일치 하는지 검사")
    @Test
    public void addAndGet() throws SQLException {
        //데이터 초기화
        this.userDao.deleteAll();
        
        //데이터 초기화 검증
        assertThat(this.userDao.getCount(), is(0));
     
        //데이터 등록
        this.userDao.add(this.user1);
        this.userDao.add(this.user2);
        
        //카운터 기능 검증
        assertThat(this.userDao.getCount(), is(2));
        
        //데이터 조회
        final User findUser1 = this.userDao.get(this.user1.id());
    
        //테스트
        assertThat(findUser1.name(), is(this.user1.name()));
        assertThat(findUser1.password(), is(this.user1.password()));
        
        //데이터 조회
        final User findUser2 = this.userDao.get(this.user2.id());
    
        //테스트
        assertThat(findUser2.name(), is(this.user2.name()));
        assertThat(findUser2.password(), is(this.user2.password()));
    }
    
    @DisplayName("DB 테이블의 레코드 수 조회 기능 검사")
    @Test
    public void count() throws SQLException {
        this.userDao.deleteAll();
        assertThat(this.userDao.getCount(), is(0));
    
        this.userDao.add(this.user1);
        assertThat(this.userDao.getCount(), is(1));
    
        this.userDao.add(this.user2);
        assertThat(this.userDao.getCount(), is(2));
    
        this.userDao.add(this.user3);
        assertThat(this.userDao.getCount(), is(3));
    }
    
    @DisplayName("없는 사용자 조회시 예외 발생 검사")
    @Test
    public void getUserFailure() throws SQLException {
        //데이터 초기화
        this.userDao.deleteAll();
        
        assertThat(this.userDao.getCount(), is(0));
    
        assertThrows(EmptyResultDataAccessException.class, () -> {
            this.userDao.get("unkown_id");
        });
    }
}
