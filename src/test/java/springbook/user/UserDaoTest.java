package springbook.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import springbook.user.dao.DaoFactory;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 테스트를 위한 main 메서드를 실행 시키기 위한 클래스
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DaoFactory.class)
public class UserDaoTest {
    @Autowired
    private UserDao userDao;
    @Autowired
    private DataSource dataSource;
    private User user1;
    private User user2;
    private User user3;
    
    @BeforeEach
    public void setUp() {
        this.user1 = new User("toby", "토비", "toby3", Level.BASIC, 1, 0, "toby@mail.com");
        this.user2 = new User("kimyh", "김영한", "kim1234", Level.SILVER, 55, 10, "kimyh@mail.com");
        this.user3 = new User("whiteship", "백기선", "white1234", Level.GOLD, 100, 40, "whiteship@mail.com");
    }
    
    @DisplayName("데이터 DB에 등록 후 조회한 결과와 등록한 결과가 일치 하는지 검사")
    @Test
    public void addAndGet() throws SQLException {
        //데이터 초기화
        this.userDao.deleteAll();
        
        //데이터 초기화 검증
        assertThat(this.userDao.getCount()).isEqualTo(0);
     
        //데이터 등록
        this.userDao.add(this.user1);
        this.userDao.add(this.user2);
        
        //카운터 기능 검증
        assertThat(this.userDao.getCount()).isEqualTo(2);
        
        //데이터 조회
        final User findUser1 = this.userDao.get(this.user1.getId());
    
        //테스트
        this.checkSameUser(this.user1, findUser1);
        
        //데이터 조회
        final User findUser2 = this.userDao.get(this.user2.getId());
    
        //테스트
        this.checkSameUser(this.user2, findUser2);
    }
    
    @DisplayName("DB 테이블의 레코드 수 조회 기능 검사")
    @Test
    public void count() {
        this.userDao.deleteAll();
        assertThat(this.userDao.getCount()).isEqualTo(0);
    
        this.userDao.add(this.user1);
        assertThat(this.userDao.getCount()).isEqualTo(1);
    
        this.userDao.add(this.user2);
        assertThat(this.userDao.getCount()).isEqualTo(2);
    
        this.userDao.add(this.user3);
        assertThat(this.userDao.getCount()).isEqualTo(3);
    }
    
    @DisplayName("없는 사용자 조회시 예외 발생 검사")
    @Test
    public void getUserFailure() {
        //데이터 초기화
        this.userDao.deleteAll();
        
        assertThat(this.userDao.getCount()).isEqualTo(0);
    
        assertThrows(EmptyResultDataAccessException.class, () -> this.userDao.get("unkown_id"));
    }
    
    @DisplayName("전체 데이터 조회 검사")
    @Test
    public void getAll() {
        //데이터 초기화
        this.userDao.deleteAll();
        
        //네거티브 테스트 - 데이터가 없을 경우 어떤 결과인지? (개발자는 예외를 발생 시키던, 빈 값을 던지던 어쨌든 하나의 기준을 둬야 한다)
        final List<User> users0 = this.userDao.getAll();
        assertThat(users0.size()).isEqualTo(0);
        
        this.userDao.add(user1);
        final List<User> users1 = this.userDao.getAll();
        assertThat(users1.size()).isEqualTo(1);
        
        this.userDao.add(user2);
        final List<User> users2 = this.userDao.getAll();
        assertThat(users2.size()).isEqualTo(2);
        checkSameUser(user2, users2.get(0)); //id값 알파벳순으로 검사하므로 user2가 user1보다 선행
        checkSameUser(user1, users2.get(1));
        
        this.userDao.add(user3);
        final List<User> users3 = this.userDao.getAll();
        assertThat(users3.size()).isEqualTo(3);
        checkSameUser(user2, users3.get(0));
        checkSameUser(user1, users3.get(1));
        checkSameUser(user3, users3.get(2));
    }
    
    @DisplayName("중복 key 등록시 예외 발생 확인")
    @Test
    public void duplicateKey() {
        //데이터 초기화
        this.userDao.deleteAll();
        
        //동일한 key의 정보를 2번 등록하여 예외 발생 확인
        assertThrows(DuplicateKeyException.class, () -> {
            this.userDao.add(user1);
            this.userDao.add(user1);
        });
    }
    
    @DisplayName("SQLException 전환 학습 테스트")
    @Test
    public void sqlExceptionTranslate() {
        //데이터 초기화
        this.userDao.deleteAll();
        
        try {
            this.userDao.add(user1);
            this.userDao.add(user1);
        } catch (final DuplicateKeyException e) {
            final SQLException sqlException = (SQLException) e.getRootCause();
            final SQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);
            
            assertThat(set.translate(null, null, sqlException)).isOfAnyClassIn(DuplicateKeyException.class);
        }
    }
    
    @DisplayName("사용자 정보 수정 테스트")
    @Test
    public void update() {
        // 데이터 초기화
        this.userDao.deleteAll();
        
        // Given
        this.userDao.add(user1);
        this.userDao.add(user2);
        
        // When
        final User updateUser = new User(user1.getId(), "오민규", "springno6", Level.GOLD, 1000, 999, "springno6@mail.com");
        
        this.userDao.update(updateUser);
        
        // Then
        final User updateAfterUser = this.userDao.get(updateUser.getId());
        
        this.checkSameUser(updateUser, updateAfterUser);
        
        final User selectedUser2 = this.userDao.get(user2.getId());
        
        this.checkSameUser(user2, selectedUser2);
    }
    
    private void checkSameUser(final User user, final User findUser) {
        assertThat(user.getId()).isEqualTo(findUser.getId());
        assertThat(user.getName()).isEqualTo(findUser.getName());
        assertThat(user.getPassword()).isEqualTo(findUser.getPassword());
        assertThat(user.getLevel()).isEqualTo(findUser.getLevel());
        assertThat(user.getLogin()).isEqualTo(findUser.getLogin());
        assertThat(user.getRecommend()).isEqualTo(findUser.getRecommend());
        assertThat(user.getEmail()).isEqualTo(findUser.getEmail());
    }
}
