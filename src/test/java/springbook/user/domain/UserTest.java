package springbook.user.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 사용자 클래스에 추가된 로직에 대한 테스트
 */
class UserTest {
    @DisplayName("사용자 레벨 업그레이드 테스트")
    @Test
    void upgradeLevel() {
        final Level[] levels = Level.values();
        
        for (final Level level : levels) {
            if (level.nextLevel() == null) {
                continue;
            }
            
            // Given
            final User user = new User(
                    "id",
                    "name",
                    "password",
                    level,
                    0,
                    0,
                    "test@mail.com"
            );
            
            // When
            user.upgradeLevel();
            
            // Then
            assertThat(user.getLevel()).isEqualTo(level.nextLevel());
        }
    }
    
    @DisplayName("업그레이드 불가 테스트")
    @Test
    public void cannotUpgradeLevel() {
        final Level[] levels = Level.values();
    
        for (final Level level : levels) {
            if (level.nextLevel() != null) {
                continue;
            }
        
            // Given
            final User user = new User(
                    "id",
                    "name",
                    "password",
                    level,
                    0,
                    0,
                    "test@mail.com"
            );
        
            // When & Then
            Assertions.assertThrows(IllegalStateException.class, user::upgradeLevel);
        }
    }
}