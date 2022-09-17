package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 커넥션 관련 유틸
 */
public class ConnectionUtils {
    /**
     * DB 연결시 사용한 자원 반납
     *
     * @param connection Connection 객체
     * @param preparedStatement PreparedStatement 객체
     *
     * @throws SQLException
     */
    public static void release(final Connection connection, final PreparedStatement preparedStatement) throws SQLException {
        release(connection, preparedStatement, null);
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
    public static void release(final Connection connection, final PreparedStatement preparedStatement, final ResultSet resultSet) {
        //ResultSet 객체 자원 반납
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (final SQLException e) {
            }
        }
    
        //PreparedStatement 객체 자원 반납
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (final SQLException e) {
            }
        }
        
        //Connection 객체 자원 반납
        if (connection != null) {
            try {
                connection.close();
            } catch (final SQLException e) {
            }
        }
    }
}
