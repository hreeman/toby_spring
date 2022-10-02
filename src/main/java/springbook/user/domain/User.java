package springbook.user.domain;

/**
 * 사용자 정보 저장용 자바 빈
 *
 * @param id 아이디
 * @param name 이름
 * @param password 비밀번호
 */
public record User(String id, String name, String password, Level level, int login, int recommend) {
}
