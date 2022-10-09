package springbook.user.service;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import springbook.user.domain.User;

public class UserServiceTx implements UserService {
    private UserService userService;
    private PlatformTransactionManager transactionManager;
    
    public void setUserService(final UserService userService) {
        this.userService = userService;
    }
    
    public void setTransactionManager(final PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
    
    @Override
    public void add(final User user) {
        this.userService.add(user);
    }
    
    @Override
    public void upgradeLevels() {
        final TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
    
        try {
            this.userService.upgradeLevels();
        
            this.transactionManager.commit(status);
        } catch (final RuntimeException e) {
            this.transactionManager.rollback(status);
        
            throw e;
        }
    }
}
