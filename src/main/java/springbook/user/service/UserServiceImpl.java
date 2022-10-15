package springbook.user.service;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;
import springbook.user.policy.UserLevelUpgradePolicy;

import java.util.List;

/**
 * 사용자 관련 비즈니스 로직을 위한 서비스
 */
public class UserServiceImpl implements UserService {
    private UserDao userDao;
    private UserLevelUpgradePolicy userLevelUpgradePolicy;
    
    private MailSender mailSender;
    
    public void setUserDao(final UserDao userDao) {
        this.userDao = userDao;
    }
    
    public void setUserLevelUpgradePolicy(final UserLevelUpgradePolicy userLevelUpgradePolicy) {
        this.userLevelUpgradePolicy = userLevelUpgradePolicy;
    }
    
    public void setMailSender(final MailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    @Override
    public void upgradeLevels() {
        final List<User> users = this.userDao.getAll();
    
        for (final User user : users) {
            if (this.userLevelUpgradePolicy.canUpgradeLevel(user)) {
                this.userLevelUpgradePolicy.upgradeLevel(user);
            
                // 레벨 업그레이드
                this.userDao.update(user);
            
                // 이메일 전송
                this.sendUpgradeEMail(user);
            }
        }
    }
    
    @Override
    public void add(final User user) {
        final User addUser;
        
        if (user.getLevel() == null) {
            addUser = new User(
                    user.getId(),
                    user.getName(),
                    user.getPassword(),
                    Level.BASIC,
                    user.getLogin(),
                    user.getRecommend(),
                    user.getEmail()
            );
        } else {
            addUser = user;
        }
        
        this.userDao.add(addUser);
    }
    
    /**
     * 사용자 레벨 업그레이드시 메일 발송
     *
     * @param user 사용자 정보 객체
     */
    private void sendUpgradeEMail(final User user) {
        final SimpleMailMessage mailMessage = new SimpleMailMessage();
        
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("useradmin@ksug.org");
        mailMessage.setSubject("Upgrade 안내");
        mailMessage.setText("사용자님의 등급이 " + user.getLevel().name() + "으로 변경 되었습니다.");
        
        this.mailSender.send(mailMessage);
    }
}
