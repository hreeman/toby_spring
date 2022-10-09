package springbook.user.service;

import springbook.user.domain.User;

public interface UserService {
    /**
     * 사용자 정보 등록 비즈니스 로직
     *
     * @param user 사용자 정보 객체
     */
    void add(final User user);
    
    /**
     * 사용자 레벨 업그레이드 메서드
     */
    void upgradeLevels();
}
