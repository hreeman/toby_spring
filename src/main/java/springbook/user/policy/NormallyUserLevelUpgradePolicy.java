package springbook.user.policy;

import springbook.user.domain.Level;
import springbook.user.domain.User;

/**
 * 평상시 사용자 레벨 업그레이드 정책
 */
public class NormallyUserLevelUpgradePolicy implements UserLevelUpgradePolicy {
    @Override
    public boolean canUpgradeLevel(final User user) {
        final Level currentLevel = user.getLevel();
    
        return switch (currentLevel) {
            case BASIC -> (user.getLogin() >= this.minLogcountForSilver());
            case SILVER -> (user.getRecommend() >= this.minRecommendForGold());
            case GOLD -> false;
            default -> throw new IllegalArgumentException("Unknown Level: " + currentLevel);
        };
    }
    
    @Override
    public void upgradeLevel(final User user) {
        user.upgradeLevel();
    }
    
    @Override
    public int minLogcountForSilver() {
        return 50;
    }
    
    @Override
    public int minRecommendForGold() {
        return 30;
    }
}
