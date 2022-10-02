package springbook.learningtest.junit;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * JUnit 자체 테스트 / Spring Test 테스트
 *
 * <pre>
 * 1. JUnit이 테스트 메소드를 수행할 때 마다 새로운 오브젝트를 생성한다고 하는데
 * 진짜 그러한가?를 위한 테스트
 *
 * 2. Spring Test의 경우 테스트시 매번 동일한 어플리케이션 컨텍스트를 재사용한다는데
 * 진짜 그러한가?를 위한 테스트
 * </pre>
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JUnitTest.JUnit.class)
public class JUnitTest {
    @Autowired
    private ApplicationContext context;
    
    static Set<JUnitTest> testObjects = new HashSet<>();
    static ApplicationContext contextObject = null;
    
    @Test
    public void test1() {
        assertThat(testObjects).doesNotContain(this);
        testObjects.add(this);
        
        assertThat((contextObject == null || contextObject == this.context)).isTrue();
        contextObject = this.context;
    }
    
    @Test
    public void test2() {
        assertThat(testObjects).doesNotContain(this);
        testObjects.add(this);
    
        assertTrue(contextObject == null || contextObject == this.context);
        contextObject = this.context;
    }
    
    @Test
    public void test3() {
        assertThat(testObjects).doesNotContain(this);
        testObjects.add(this);
    
        assertTrue(contextObject == null || contextObject == this.context);
        contextObject = this.context;
    }
    
    @Configuration
    static class JUnit {
    
    }
}
