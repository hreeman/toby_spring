package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteAllStatement implements StatementStrategy {
    @Override
    public PreparedStatement makePreparedStatement(final Connection connection) throws SQLException {
        final PreparedStatement preparedStatement = connection.prepareStatement(
                "DELETE FROM users"
        );
        
        return preparedStatement;
    }
}
