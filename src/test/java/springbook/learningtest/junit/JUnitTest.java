package springbook.learningtest.junit;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
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
    static JUnitTest testObject;
    
    @Test
    public void test1() {
        assertThat(this, is(not(sameInstance(testObject))));
        testObject = this;
    }
    
    @Test
    public void test2() {
        assertThat(this, is(not(sameInstance(testObject))));
        testObject = this;
    }
    
    @Test
    public void test3() {
        assertThat(this, is(not(sameInstance(testObject))));
        testObject = this;
    }
}
