package springbook.user.exception;

/**
 * 사용자 ID가 중복일 경우 발생되는 Exception
 */
public class DuplicateUserIdException extends RuntimeException {
    public DuplicateUserIdException(final Throwable cause) {
        super(cause);
    }
}
