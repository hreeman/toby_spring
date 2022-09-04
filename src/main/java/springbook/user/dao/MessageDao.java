package springbook.user.dao;

/**
 * Message Dao
 */
public class MessageDao {
    private final ConnectionMaker connectionMaker;
    
    public MessageDao(final ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }
}
