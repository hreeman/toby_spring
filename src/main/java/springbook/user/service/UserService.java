package springbook.user.service;

import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import java.util.List;

/**
 * 사용자 관련 비즈니스 로직을 위한 서비스
 */
public class UserService {
    private UserDao userDao;
    
    public void setUserDao(final UserDao userDao) {
        this.userDao = userDao;
    }
    
    /**
     * 사용자 레벨 업그레이드 메서드
     */
    public void upgradeLevels() {
        final List<User> users = this.userDao.getAll();
        
        for (final User user : users) {
            if (this.canUpgradeLevel(user)) {
                this.upgradeLevel(user);
            }
        }
    }

    /**
     * 레벨 업그레이드 가능 유무 확인 메서드
     *
     * @param user 사용자 정보 객체
     *
     * @return 레벨 업그레이드 가능유무 (가능이면 true, 불가능이면 false)
     */
    private boolean canUpgradeLevel(final User user) {
        final Level currentLevel = user.level();
        
        return switch (currentLevel) {
            case BASIC -> (user.login() >= 50);
            case SILVER -> (user.recommend() >= 30);
            case GOLD -> false;
            default -> throw new IllegalArgumentException("Unknown Level: " + currentLevel);
        };
    }
    
    /**
     * 실제 레벨 업그레이드 작업을 하는 메서드
     *
     * @param user 사용자 정보 객체
     */
    private void upgradeLevel(final User user) {
        final User updateUser = user.upgradeLevel();
        
        this.userDao.update(updateUser);
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
