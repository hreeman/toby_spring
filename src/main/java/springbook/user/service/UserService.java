package springbook.user.service;

import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;
import springbook.user.policy.UserLevelUpgradePolicy;

import java.util.List;

/**
 * 사용자 관련 비즈니스 로직을 위한 서비스
 */
public class UserService {
    private UserDao userDao;
    private UserLevelUpgradePolicy userLevelUpgradePolicy;
    
    public void setUserDao(final UserDao userDao) {
        this.userDao = userDao;
    }
    
    public void setUserLevelUpgradePolicy(final UserLevelUpgradePolicy userLevelUpgradePolicy) {
        this.userLevelUpgradePolicy = userLevelUpgradePolicy;
    }
    
    /**
     * 사용자 레벨 업그레이드 메서드
     */
    public void upgradeLevels() {
        final List<User> users = this.userDao.getAll();
        
        for (final User user : users) {
            if (this.userLevelUpgradePolicy.canUpgradeLevel(user)) {
                final User updateUser = this.userLevelUpgradePolicy.upgradeLevel(user);
                
                this.userDao.update(updateUser);
            }
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
