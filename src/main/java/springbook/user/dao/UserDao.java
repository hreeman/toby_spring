package springbook.user.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * JDBC를 이용하여 사용자 정보를 DB에 넣고 관리할 DAO
 */
public class UserDao {
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;
    
    public void setDataSource(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        
        this.dataSource = dataSource;
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
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;
        
        try {
            connection = this.dataSource.getConnection();
    
            preparedStatement = connection.prepareStatement(
                    "SELECT * FROM users WHERE id = ?"
            );
    
            preparedStatement.setString(1, id);
    
            resultSet = preparedStatement.executeQuery();
    
            if (resultSet.next()) {
                user = new User(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("password")
                );
            }
    
            if (Objects.isNull(user)) {
                throw new EmptyResultDataAccessException(1);
            }
    
            return user;
        } catch(final SQLException e) {
            throw e;
        } finally {
            ConnectionUtils.release(connection, preparedStatement, resultSet);
        }
    }
    
    /**
     * 사용자 정보 DB 에서 삭제
     *
     * @deprecated 전체삭제 기능 추가에 따른 미사용. 추후 제거
     *
     * @param id 아이디
     *
     * @throws SQLException
     */
    @Deprecated
    public void remove(final String id) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        
        try {
            connection = this.dataSource.getConnection();
    
            preparedStatement = connection.prepareStatement(
                    "DELETE FROM users WHERE id = ?"
            );
    
            preparedStatement.setString(1, id);
    
            preparedStatement.executeUpdate();
        } catch(final SQLException e) {
            throw e;
        } finally {
            ConnectionUtils.release(connection, preparedStatement);
        }
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
}
