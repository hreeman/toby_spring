package springbook.user.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * JDBC 작업 흐름을 분리한 클래스
 */
public class JdbcContext {
    private DataSource dataSource;
    
    public void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    public void workWithStatementStrategy(final StatementStrategy strategy) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
    
        try {
            connection = this.dataSource.getConnection();
        
            preparedStatement = strategy.makePreparedStatement(connection);
        
            preparedStatement.executeUpdate();
        } catch (final SQLException e) {
            throw e;
        } finally {
            ConnectionUtils.release(connection, preparedStatement);
        }
    }
}
