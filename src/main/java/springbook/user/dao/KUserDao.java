package springbook.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * K사 User DAO
 */
public class KUserDao extends UserDao {
    @Override
    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mariadb://localhost:3306/toby_spring", "toby", "toby");
    }
}
