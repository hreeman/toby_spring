package springbook.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import springbook.user.dao.DaoFactory;
import springbook.user.dao.UserDao;
import springbook.user.domain.User;

import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * 테스트를 위한 main 메서드를 실행 시키기 위한 클래스
 */
public class UserDaoTest {
    @DisplayName("데이터 DB에 등록 후 조회한 결과와 등록한 결과가 일치 하는지 검사")
    @Test
    public void addAndGet() throws SQLException {
        //테스트 할 UserDao 빈 획득
        final ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        final UserDao userDao = context.getBean("userDao", UserDao.class);
        
        //데이터 초기화
        userDao.deleteAll();
        
        //데이터 초기화 검증
        assertThat(userDao.getCount(), is(0));
        
        //등록할 데이터 생성
        final User user = new User("toby", "토비", "toby3");
        
        //데이터 등록
        userDao.add(user);
        
        //카운터 기능 검증
        assertThat(userDao.getCount(), is(1));
        
        //데이터 조회
        final User findUser = userDao.get(user.id());
    
        //테스트
        assertThat(findUser.name(), is(user.name()));
        assertThat(findUser.password(), is(user.password()));
    }
}
