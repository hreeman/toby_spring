package springbook.user.dao;

import springbook.user.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * JDBC를 이용하여 사용자 정보를 DB에 넣고 관리할 DAO
 */
public class UserDao {
    private final ConnectionMaker connectionMaker;
    
    public UserDao(final ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }
    
    /**
     * 사용자 정보 DB 등록
     *
     * @param user 사용자 정보가 담긴 User 객체
     *
     * @throws SQLException
     */
    public void add(final User user) throws SQLException {
        final Connection connection = this.connectionMaker.makeNewConnection();
        
        final PreparedStatement preparedStatement = connection.prepareStatement(
            "INSERT INTO users(id, name, password) VALUES (?, ?, ?)"
        );
        
        preparedStatement.setString(1, user.id());
        preparedStatement.setString(2, user.name());
        preparedStatement.setString(3, user.password());
        
        preparedStatement.executeUpdate();
        
        this.connectionMaker.release(connection, preparedStatement);
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
        final Connection connection = this.connectionMaker.makeNewConnection();
        
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
        
        this.connectionMaker.release(connection, preparedStatement, resultSet);
        
        return user;
    }
    
    /**
     * 사용자 정보 DB 에서 삭제
     *
     * @param id 아이디
     *
     * @throws SQLException
     */
    public void remove(final String id) throws SQLException {
        final Connection connection = this.connectionMaker.makeNewConnection();
        
        final PreparedStatement preparedStatement = connection.prepareStatement(
                "DELETE FROM users WHERE id = ?"
        );
        
        preparedStatement.setString(1, id);
        
        preparedStatement.executeUpdate();
        
        this.connectionMaker.release(connection, preparedStatement);
    }
}
