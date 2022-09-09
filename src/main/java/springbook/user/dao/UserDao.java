package springbook.user.dao;

import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * JDBC를 이용하여 사용자 정보를 DB에 넣고 관리할 DAO
 */
public class UserDao {
    private DataSource dataSource;
    
    public void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    /**
     * 사용자 정보 DB 등록
     *
     * @param user 사용자 정보가 담긴 User 객체
     *
     * @throws SQLException
     */
    public void add(final User user) throws SQLException {
        final Connection connection = this.dataSource.getConnection();
        
        final PreparedStatement preparedStatement = connection.prepareStatement(
            "INSERT INTO users(id, name, password) VALUES (?, ?, ?)"
        );
        
        preparedStatement.setString(1, user.id());
        preparedStatement.setString(2, user.name());
        preparedStatement.setString(3, user.password());
        
        preparedStatement.executeUpdate();
    
        ConnectionUtils.release(connection, preparedStatement);
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
        final Connection connection = this.dataSource.getConnection();
        
        final PreparedStatement preparedStatement = connection.prepareStatement(
            "SELECT * FROM users WHERE id = ?"
        );
        
        preparedStatement.setString(1, id);
        
        final ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        
        final User user = new User(
            resultSet.getString("id"),
            resultSet.getString("name"),
            resultSet.getString("password")
        );
    
        ConnectionUtils.release(connection, preparedStatement, resultSet);
        
        return user;
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
        final Connection connection = this.dataSource.getConnection();
        
        final PreparedStatement preparedStatement = connection.prepareStatement(
                "DELETE FROM users WHERE id = ?"
        );
        
        preparedStatement.setString(1, id);
        
        preparedStatement.executeUpdate();
        
        ConnectionUtils.release(connection, preparedStatement);
    }
    
    /**
     * DB 사용자 정보 테이블 데이터 전체 삭제
     *
     * @throws SQLException
     */
    public void deleteAll() throws SQLException {
        final Connection connection = this.dataSource.getConnection();
        
        final PreparedStatement preparedStatement = connection.prepareStatement(
                "DELETE FROM users"
        );
        
        preparedStatement.executeUpdate();
        
        ConnectionUtils.release(connection, preparedStatement);
    }
    
    /**
     * DB 사용자 정보 테이블 레코드 갯수 조회
     *
     * @return 조회된 사용자 정보 전체 테이블 레코드 수
     *
     * @throws SQLException
     */
    public int getCount() throws SQLException {
        final Connection connection = this.dataSource.getConnection();
        
        final PreparedStatement preparedStatement = connection.prepareStatement(
                "SELECT COUNT(*) FROM users"
        );
        
        final ResultSet resultSet = preparedStatement.executeQuery();
        
        resultSet.next();
        
        final int count = resultSet.getInt(1);
        
        ConnectionUtils.release(connection, preparedStatement, resultSet);
        
        return count;
    }
}
