package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * N사 커넥션 메이커
 */
public class NConnectionMaker implements ConnectionMaker {
    @Override
    public Connection makeNewConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mariadb://localhost:3306/toby_spring", "toby", "toby");
    }
}
