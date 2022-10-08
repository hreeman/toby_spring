package springbook.user.domain;

/**
 * 사용자 레벨 enum
 */
public enum Level {
    GOLD(3, null),
    SILVER(2, GOLD),
    BASIC(1, SILVER);
    
    private final int value;
    private final Level next; //다음 단계의 레벨 정보를 스스로 갖도록 함
    
    Level(final int value, final Level next) {
        this.value = value;
        this.next = next;
    }
    
    public int intValue() {
        return value;
    }
    
    public Level nextLevel() {
        return this.next;
    }
    
    public static Level valueOf(final int value) {
        return switch (value) {
            case 1 -> BASIC;
            case 2 -> SILVER;
            case 3 -> GOLD;
            default -> throw new AssertionError("Unknown value: " + value);
        };
    }
}
