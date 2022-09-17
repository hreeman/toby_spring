package springbook.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import springbook.user.dao.UserDao;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 테스트를 위한 main 메서드를 실행 시키기 위한 클래스
 */
public class UserDaoTest {
    private UserDao userDao;
    private User user1;
    private User user2;
    private User user3;
    
    @BeforeEach
    public void setUp() {
        this.user1 = new User("toby", "토비", "toby3");
        this.user2 = new User("kimyh", "김영한", "kim1234");
        this.user3 = new User("whiteship", "백기선", "white1234");
        
        this.userDao = new UserDao();
    
        final DataSource dataSource = new SingleConnectionDataSource(
                "jdbc:mariadb://localhost:3306/testdb",
                "toby",
                "toby",
                true
        );
        
        this.userDao.setDataSource(dataSource);
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
    public void count() {
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
    public void getUserFailure() {
        //데이터 초기화
        this.userDao.deleteAll();
        
        assertThat(this.userDao.getCount(), is(0));
    
        assertThrows(EmptyResultDataAccessException.class, () -> {
            this.userDao.get("unkown_id");
        });
    }
    
    @DisplayName("전체 데이터 조회 검사")
    @Test
    public void getAll() {
        //데이터 초기화
        this.userDao.deleteAll();
        
        //네거티브 테스트 - 데이터가 없을 경우 어떤 결과인지? (개발자는 예외를 발생 시키던, 빈 값을 던지던 어쨌든 하나의 기준을 둬야 한다)
        final List<User> users0 = this.userDao.getAll();
        assertThat(users0.size(), is(0));
        
        this.userDao.add(user1);
        final List<User> users1 = this.userDao.getAll();
        assertThat(users1.size(), is(1));
        
        this.userDao.add(user2);
        final List<User> users2 = this.userDao.getAll();
        assertThat(users2.size(), is(2));
        checkSameUser(user2, users2.get(0)); //id값 알파벳순으로 검사하므로 user2가 user1보다 선행
        checkSameUser(user1, users2.get(1));
        
        this.userDao.add(user3);
        final List<User> users3 = this.userDao.getAll();
        assertThat(users3.size(), is(3));
        checkSameUser(user2, users3.get(0));
        checkSameUser(user1, users3.get(1));
        checkSameUser(user3, users3.get(2));
    }
    
    private void checkSameUser(final User user, final User findUser) {
        assertThat(user.id(), is(findUser.id()));
        assertThat(user.name(), is(findUser.name()));
        assertThat(user.password(), is(findUser.password()));
    }
}
