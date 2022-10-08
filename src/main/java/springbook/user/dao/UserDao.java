package springbook.user.dao;

import springbook.user.domain.User;

import java.util.List;

/**
 * 사용자 정보를 DB에 넣고 관리할 DAO
 */
public interface UserDao {
    /**
     * 사용자 정보 DB 등록
     *
     * @param user 사용자 정보가 담긴 User 객체
     */
    void add(User user);
    
    /**
     * id를 이용하여 사용자 정보를 DB에서 조회
     *
     * @param id 아이디
     *
     * @return 사용자 정보 User 객체
     */
    User get(String id);
    
    /**
     * DB 사용자 정보 전체 목록 조회
     *
     * @return 사용자 정보 전체 목록
     */
    List<User> getAll();
    
    /**
     * DB 사용자 정보 테이블 데이터 전체 삭제
     */
    void deleteAll();
    
    /**
     * DB 사용자 정보 테이블 레코드 갯수 조회
     *
     * @return 조회된 사용자 정보 전체 테이블 레코드 수
     */
    int getCount();
    
    /**
     * 수정된 사용자 정보 User 객체를 이용하여 사용자 정보 DB 업데이트
     *
     * @param updateUser 수정된 사용자 정보 User 객체
     */
    void update(User updateUser);
}
