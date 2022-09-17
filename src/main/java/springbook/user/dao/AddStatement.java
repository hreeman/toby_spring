package springbook.user.dao;

import springbook.user.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddStatement implements StatementStrategy {
    private final User user;
    
    public AddStatement(final User user) {
        this.user = user;
    }
    
    @Override
    public PreparedStatement makePreparedStatement(final Connection connection) throws SQLException {
        
        
        
        final PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO users(id, name, password) VALUES (?, ?, ?)"
        );
    
        preparedStatement.setString(1, this.user.id());
        preparedStatement.setString(2, this.user.name());
        preparedStatement.setString(3, this.user.password());
    
        return preparedStatement;
    }
}
