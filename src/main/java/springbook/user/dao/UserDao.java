package springbook.user.dao;

import springbook.user.domain.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * JDBC를 이용하여 사용자 정보를 DB에 넣고 관리할 DAO
 */
public abstract class UserDao {
    /**
     * 사용자 정보 DB 등록
     *
     * @param user 사용자 정보가 담긴 User 객체
     *
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public void add(final User user) throws ClassNotFoundException, SQLException {
        final Connection connection = this.getConnection();
        
        final PreparedStatement preparedStatement = connection.prepareStatement(
            "INSERT INTO users(id, name, password) VALUES (?, ?, ?)"
        );
        
        preparedStatement.setString(1, user.id());
        preparedStatement.setString(2, user.name());
        preparedStatement.setString(3, user.password());
        
        preparedStatement.executeUpdate();
        
        this.close(connection, preparedStatement);
    }
    
    /**
     * id를 이용하여 사용자 정보를 DB에서 조회
     *
     * @param id 아이디
     *
     * @return 사용자 정보 User 객체
     *
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public User get(final String id) throws ClassNotFoundException, SQLException {
        final Connection connection = this.getConnection();
        
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
        
        this.close(connection, preparedStatement, resultSet);
        
        return user;
    }
    
    /**
     * 커넥션 얻어오는 메서드
     *
     * <pre>
     * URL, ID, PASSWORD등을 입력받아 사용하는 방식도 고려했으나
     * 시작하는 간단한 예제이므로 생략
     * </pre>
     *
     * @return Connection 객체
     *
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    protected abstract Connection getConnection() throws ClassNotFoundException, SQLException;
    
    /**
     * DB 연결시 사용한 자원 반납
     *
     * @param connection Connection 객체
     * @param preparedStatement PreparedStatement 객체
     *
     * @throws SQLException
     */
    private void close(final Connection connection, final PreparedStatement preparedStatement) throws SQLException {
        this.close(connection, preparedStatement, null);
    }
    
    /**
     * DB 연결시 사용한 자원 반납
     *
     * @param connection Connection 객체
     * @param preparedStatement PreparedStatement 객체
     * @param resultSet ResultSet 객체
     *
     * @throws SQLException
     */
    private void close(final Connection connection, final PreparedStatement preparedStatement, final ResultSet resultSet) throws SQLException {
        //Connection 객체 자원 반납
        if (connection != null) {
            connection.close();
        }
    
        //PreparedStatement 객체 자원 반납
        if (preparedStatement != null) {
            preparedStatement.close();
        }
    
        //ResultSet 객체 자원 반납
        if (resultSet != null) {
            resultSet.close();
        }
    }
}
