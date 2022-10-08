package springbook.user.service;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;
import springbook.user.policy.UserLevelUpgradePolicy;

import javax.sql.DataSource;
import java.util.List;

/**
 * 사용자 관련 비즈니스 로직을 위한 서비스
 */
public class UserService {
    private UserDao userDao;
    private UserLevelUpgradePolicy userLevelUpgradePolicy;
    
    private DataSource dataSource;
    
    public void setUserDao(final UserDao userDao) {
        this.userDao = userDao;
    }
    
    public void setUserLevelUpgradePolicy(final UserLevelUpgradePolicy userLevelUpgradePolicy) {
        this.userLevelUpgradePolicy = userLevelUpgradePolicy;
    }
    
    public void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    /**
     * 사용자 레벨 업그레이드 메서드
     */
    public void upgradeLevels() throws Exception {
        final PlatformTransactionManager transactionManager = new DataSourceTransactionManager(this.dataSource);
        final TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        
        try {
            final List<User> users = this.userDao.getAll();
            
            for (final User user : users) {
                if (this.userLevelUpgradePolicy.canUpgradeLevel(user)) {
                    final User updateUser = this.userLevelUpgradePolicy.upgradeLevel(user);
                    
                    this.userDao.update(updateUser);
                }
            }
            
            transactionManager.commit(status);
        } catch (final Exception e) {
            transactionManager.rollback(status);
            
            throw e;
        }
    }
    
    /**
     * 사용자 정보 등록 비즈니스 로직
     *
     * @param user 사용자 정보 객체
     */
    public void add(final User user) {
        final User addUser;
        
        if (user.level() == null) {
            addUser = new User(
                    user.id(),
                    user.name(),
                    user.password(),
                    Level.BASIC,
                    user.login(),
                    user.recommend()
            );
        } else {
            addUser = user;
        }
        
        this.userDao.add(addUser);
    }
}
