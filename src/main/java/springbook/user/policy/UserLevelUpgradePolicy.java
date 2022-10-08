package springbook.user.policy;

import springbook.user.domain.User;

/**
 * 사용자 레벨 업그레이드 정책 인터페이스
 */
public interface UserLevelUpgradePolicy {
    /**
     * 레벨 업그레이드 가능 유무 확인 메서드
     *
     * @param user 사용자 정보 객체
     *
     * @return 레벨 업그레이드 가능유무 (가능이면 true, 불가능이면 false)
     */
    boolean canUpgradeLevel(User user);
    
    /**
     * 실제 레벨 업그레이드 작업을 하는 메서드
     *
     * @param user 사용자 정보 객체
     *
     * @return 레벨 업그레이드 정보가 담긴 새로운 사용자 정보 객체
     */
    User upgradeLevel(User user);
    
    /**
     * 실버 등급으로 업그레이드 하기 위한 로그인 카운트
     *
     * @return 실버 등급으로 업그레이드 하기 위한 로그인 카운트
     */
    default int minLogcountForSilver() {
        return Integer.MAX_VALUE;
    }
    
    /**
     * 골드 등급으로 업그레이드 하기 위한 추천 카운트
     *
     * @return 골드 등급으로 업그레이드 하기 위한 추천 카운트
     */
    default int minRecommendForGold() {
        return Integer.MAX_VALUE;
    }
}
