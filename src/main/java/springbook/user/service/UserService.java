package springbook.user.service;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;
import springbook.user.policy.UserLevelUpgradePolicy;

import javax.sql.DataSource;
import java.sql.Connection;
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
        // 트랜잭션 동기화 관리자를 히용하여 동기화 작업 초기화
        TransactionSynchronizationManager.initSynchronization();
        
        // DB 커넥션 생성 및 트랜잭션 시작
        final Connection connection = DataSourceUtils.getConnection(this.dataSource);
        connection.setAutoCommit(false);
        
        try {
            final List<User> users = this.userDao.getAll();
            
            for (final User user : users) {
                if (this.userLevelUpgradePolicy.canUpgradeLevel(user)) {
                    final User updateUser = this.userLevelUpgradePolicy.upgradeLevel(user);
                    
                    this.userDao.update(updateUser);
                }
            }
            
            connection.commit();
        } catch (final Exception e) {
            connection.rollback();
            
            throw e;
        } finally {
            // 스프링 유틸리티 메서드를 이용하여 DB 커넥션을 안전하게 닫기
            DataSourceUtils.releaseConnection(connection, dataSource);
            
            // 동기화 작업 종료 및 정리
            TransactionSynchronizationManager.unbindResource(this.dataSource);
            TransactionSynchronizationManager.clearSynchronization();
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
