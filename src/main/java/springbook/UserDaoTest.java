package springbook;

import springbook.user.dao.DaoFactory;
import springbook.user.dao.UserDao;
import springbook.user.domain.User;

import java.sql.SQLException;

/**
 * 테스트를 위한 main 메서드를 실행 시키기 위한 클래스
 */
public class UserDaoTest {
    public static void main(final String[] args) throws SQLException {
        final UserDao userDao = new DaoFactory().userDao();
        
        final User user = new User("toby", "토비", "toby3");
        
        userDao.remove(user.id());
        System.out.println(user.id() + " 삭제 성공");
        System.out.println();
        
        userDao.add(user);
    
        System.out.println(user.id() + " 등록 성공");
        System.out.println();
        
        final User findUser = userDao.get(user.id());
    
        if (!user.name().equals(findUser.name())) {
            System.out.println("테스트 실패 (name)");
        } else if (!user.password().equals(findUser.password())) {
            System.out.println("테스트 실패 (password)");
        } else {
            System.out.println("조회 테스트 성공");
        }
    }
}
