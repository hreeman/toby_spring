package springbook.user.dao;

import javax.sql.DataSource;

/**
 * Message Dao
 */
public class MessageDao {
    private DataSource dataSource;
    
    public void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
