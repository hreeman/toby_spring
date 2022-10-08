package springbook.user.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.util.List;

/**
 * JDBC를 이용하여 사용자 정보를 DB에 넣고 관리할 DAO 구현체
 */
public class UserDaoJdbc implements UserDao {
    /**
     * JDBC 결과 ResultSet을 User 객체로 매핑해주는 매퍼
     */
    private final RowMapper<User> userRowMapper = (resultSet, rowNumber) -> new User(
            resultSet.getString("id"),
            resultSet.getString("name"),
            resultSet.getString("password"),
            Level.valueOf(resultSet.getInt("level")),
            resultSet.getInt("login"),
            resultSet.getInt("recommend"),
            resultSet.getString("email")
    );
    
    private JdbcTemplate jdbcTemplate;
    
    public void setDataSource(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    @Override
    public void add(final User user) {
        this.jdbcTemplate.update(
                "INSERT INTO users(id, name, password, level, login, recommend, email) VALUES (?, ?, ?, ?, ?, ?, ?)",
                user.id(),
                user.name(),
                user.password(),
                user.level().intValue(),
                user.login(),
                user.recommend(),
                user.email()
        );
    }
    
    @Override
    public User get(final String id) {
        return this.jdbcTemplate.queryForObject(
                "SELECT * FROM users WHERE id = ?",
                this.userRowMapper,
                id
        );
    }
    
    @Override
    public void deleteAll() {
        this.jdbcTemplate.update("DELETE FROM users");
    }
    
    @Override
    public int getCount() {
        return this.jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Integer.class);
    }
    
    @Override
    public void update(final User updateUser) {
        this.jdbcTemplate.update(
                "UPDATE users SET name = ?, password = ?, level = ?, login = ?, recommend = ?, email = ? where id = ?",
                updateUser.name(),
                updateUser.password(),
                updateUser.level().intValue(),
                updateUser.login(),
                updateUser.recommend(),
                updateUser.email(),
                updateUser.id()
        );
    }
    
    @Override
    public List<User> getAll() {
        return this.jdbcTemplate.query(
                "SELECT * FROM users ORDER BY id",
                this.userRowMapper
        );
    }
}
