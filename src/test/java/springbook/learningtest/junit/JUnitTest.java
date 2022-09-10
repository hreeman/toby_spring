package springbook.learningtest.junit;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * JUnit 자체 테스트
 *
 * <pre>
 * JUnit이 테스트 메소드를 수행할 때 마다 새로운 오브젝트를 생성한다고 하는데
 * 진짜 그러한가?를 위한 테스트
 * </pre>
 */
public class JUnitTest {
    static Set<JUnitTest> testObjects = new HashSet<>();
    
    @Test
    public void test1() {
        assertThat(testObjects, not(hasItem(this)));
        testObjects.add(this);
    }
    
    @Test
    public void test2() {
        assertThat(testObjects, not(hasItem(this)));
        testObjects.add(this);
    }
    
    @Test
    public void test3() {
        assertThat(testObjects, not(hasItem(this)));
        testObjects.add(this);
    }
}
