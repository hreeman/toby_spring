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
            final boolean changed; // 레벨 변화 체크
            final User updateUser;
            
            if (user.level() == Level.GOLD) { // GOLD 레벨은 변경이 발생하지 않음
                continue;
            }
            
            if ((user.level() == Level.BASIC) && (user.login() >= 50)) {
                updateUser = new User(
                        user.id(),
                        user.name(),
                        user.password(),
                        Level.SILVER,
                        user.login(),
                        user.recommend()
                );
                
                changed = true;
            } else if ((user.level() == Level.SILVER) && (user.recommend() >= 30)) {
                updateUser = new User(
                        user.id(),
                        user.name(),
                        user.password(),
                        Level.GOLD,
                        user.login(),
                        user.recommend()
                );
                
                changed = true;
            } else {
                updateUser = user;
                changed = false;
            }
            
            if (changed) {
                this.userDao.update(updateUser);
            }
        }
    }
}
