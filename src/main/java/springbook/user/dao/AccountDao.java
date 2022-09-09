package springbook.user.dao;

import javax.sql.DataSource;

/**
 * Account Dao
 */
public class AccountDao {
    private DataSource dataSource;
    
    public void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
