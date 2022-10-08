package springbook.user.domain;

/**
 * 사용자 정보 저장용 자바 빈
 *
 * @param id 아이디
 * @param name 이름
 * @param password 비밀번호
 */
public record User(String id, String name, String password, Level level, int login, int recommend) {
    
    /**
     * 레벨 업그레이드 요청
     *
     * <pre>
     * record는 불변객체 이므로, 레벨이 업그레이드 된 새로운 객체를 리턴한다.
     * </pre>
     *
     * @return 레벨이 변경된 새로운 사용자 객체
     */
    public User upgradeLevel() {
        final Level nextLevel = this.level.nextLevel();
        
        if (nextLevel == null) {
            throw new IllegalStateException(this.level + "은 업그레이드가 불가능 합니다.");
        }
        
        return new User(
                this.id,
                this.name,
                this.password,
                nextLevel,
                this.login,
                this.recommend
        );
    }
}
