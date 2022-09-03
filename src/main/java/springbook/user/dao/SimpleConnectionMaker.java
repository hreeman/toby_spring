package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 커넥션을 생성/관리하기 위한 클래스
 */
public class SimpleConnectionMaker {
    /** 생성자를 통한 생성 방지 */
    private SimpleConnectionMaker() {
        throw new IllegalStateException("생성할 수 없는 클래스 입니다.");
    }
    
    /**
     * 새로운 커넥션을 생성
     *
     * @return 새롭게 생성된 Connection 객체
     *
     * @throws SQLException
     */
    public static Connection makeNewConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mariadb://localhost:3306/toby_spring", "toby", "toby");
    }
    
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
    public static void release(final Connection connection, final PreparedStatement preparedStatement, final ResultSet resultSet) throws SQLException {
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
