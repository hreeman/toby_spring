package springbook.user.dao;

/**
 * Account Dao
 */
public class AccountDao {
    private final ConnectionMaker connectionMaker;
    
    public AccountDao(final ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }
}
