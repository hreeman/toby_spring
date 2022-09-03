package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * K사 커넥션 메이커
 */
public class KConnectionMaker implements ConnectionMaker {
    @Override
    public Connection makeNewConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mariadb://localhost:3306/toby_spring", "toby", "toby");
    }
}
