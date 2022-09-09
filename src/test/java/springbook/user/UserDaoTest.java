package springbook.user;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
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
public class UserDaoTest {
    private static UserDao userDao;
    
    @BeforeAll
    public static void setUp() {
        final ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        
        userDao = context.getBean("userDao", UserDao.class);
    }
    
    @DisplayName("데이터 DB에 등록 후 조회한 결과와 등록한 결과가 일치 하는지 검사")
    @Test
    public void addAndGet() throws SQLException {
        //데이터 초기화
        userDao.deleteAll();
        
        //데이터 초기화 검증
        assertThat(userDao.getCount(), is(0));
        
        //등록할 데이터 생성
        final User user1 = new User("toby", "토비", "toby3");
        final User user2 = new User("whiteship", "백기선", "white1234");
        
        //데이터 등록
        userDao.add(user1);
        userDao.add(user2);
        
        //카운터 기능 검증
        assertThat(userDao.getCount(), is(2));
        
        //데이터 조회
        final User findUser1 = userDao.get(user1.id());
    
        //테스트
        assertThat(findUser1.name(), is(user1.name()));
        assertThat(findUser1.password(), is(user1.password()));
        
        //데이터 조회
        final User findUser2 = userDao.get(user2.id());
    
        //테스트
        assertThat(findUser2.name(), is(user2.name()));
        assertThat(findUser2.password(), is(user2.password()));
    }
    
    @DisplayName("DB 테이블의 레코드 수 조회 기능 검사")
    @Test
    public void count() throws SQLException {
        final User user1 = new User("gyumee", "박성철", "park1234");
        final User user2 = new User("kimyh", "김영한", "kim1234");
        final User user3 = new User("whiteship", "백기선", "white1234");
    
        userDao.deleteAll();
        assertThat(userDao.getCount(), is(0));
    
        userDao.add(user1);
        assertThat(userDao.getCount(), is(1));
    
        userDao.add(user2);
        assertThat(userDao.getCount(), is(2));
    
        userDao.add(user3);
        assertThat(userDao.getCount(), is(3));
    }
    
    @DisplayName("없는 사용자 조회시 예외 발생 검사")
    @Test
    public void getUserFailure() throws SQLException {
        //데이터 초기화
        userDao.deleteAll();
        
        assertThat(userDao.getCount(), is(0));
    
        assertThrows(EmptyResultDataAccessException.class, () -> {
            userDao.get("unkown_id");
        });
    }
}
