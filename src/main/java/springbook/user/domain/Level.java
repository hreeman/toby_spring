package springbook.user.domain;

/**
 * 사용자 레벨 enum
 */
public enum Level {
    BASIC(1),
    SILVER(2),
    GOLD(3);
    
    private final int value;
    
    Level(final int value) {
        this.value = value;
    }
    
    public int intValue() {
        return value;
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
