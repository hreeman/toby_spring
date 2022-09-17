package springbook.user.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

/**
 * JDBC를 이용하여 사용자 정보를 DB에 넣고 관리할 DAO
 */
public class UserDao {
    private JdbcTemplate jdbcTemplate;
    
    public void setDataSource(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    /**
     * 사용자 정보 DB 등록
     *
     * @param user 사용자 정보가 담긴 User 객체
     *
     */
    public void add(final User user) {
        this.jdbcTemplate.update(
                "INSERT INTO users(id, name, password) VALUES (?, ?, ?)",
                user.id(),
                user.name(),
                user.password()
        );
    }
    
    /**
     * id를 이용하여 사용자 정보를 DB에서 조회
     *
     * @param id 아이디
     *
     * @return 사용자 정보 User 객체
     *
     * @throws SQLException
     */
    public User get(final String id) throws SQLException {
        return this.jdbcTemplate.queryForObject(
                "SELECT * FROM users WHERE id = ?",
                (resultSet, rowNumber) -> new User(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("password")
                ),
                id
        );
    }
    
    /**
     * DB 사용자 정보 테이블 데이터 전체 삭제
     *
     */
    public void deleteAll() {
        this.jdbcTemplate.update("DELETE FROM users");
    }
    
    /**
     * DB 사용자 정보 테이블 레코드 갯수 조회
     *
     * @return 조회된 사용자 정보 전체 테이블 레코드 수
     *
     */
    public int getCount() {
        return this.jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Integer.class);
    }
    
    /**
     * DB 사용자 정보 전체 목록 조회
     *
     * @return 사용자 정보 전체 목록
     */
    public List<User> getAll() {
        return this.jdbcTemplate.query(
                "SELECT * FROM users ORDER BY id",
                (resultSet, rowNumber) -> new User(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("password")
                )
        );
        
    }
}
