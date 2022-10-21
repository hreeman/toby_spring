package springbook.learningtest.jdk.proxy;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import springbook.learningtest.jdk.Hello;
import springbook.learningtest.jdk.HelloTarget;
import springbook.learningtest.jdk.UppercaseHandler;

import java.lang.reflect.Proxy;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("스프링 ProxyFactoryBean 학습")
public class DynamicProxyTest {
    @DisplayName("JDK 다이내믹 프록시 생성 테스트")
    @Test
    public void simpleProxy() {
        final Hello proxiedHello = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[] { Hello.class },
                new UppercaseHandler(new HelloTarget())
        );
    
        assertThat(proxiedHello.sayHello("Toby")).isEqualTo("HELLO TOBY");
        assertThat(proxiedHello.sayHi("Toby")).isEqualTo("HI TOBY");
        assertThat(proxiedHello.sayThankYou("Toby")).isEqualTo("THANK YOU TOBY");
    }
    
    @DisplayName("Spring ProxyFactoryBean 테스트")
    @Test
    public void proxyFactoryBean() {
        final ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        
        proxyFactoryBean.setTarget(new HelloTarget());
        proxyFactoryBean.addAdvice(new UppercaseAdvice());
        
        final Hello proxiedHello = (Hello) proxyFactoryBean.getObject();
    
        assertThat(proxiedHello.sayHello("Toby")).isEqualTo("HELLO TOBY");
        assertThat(proxiedHello.sayHi("Toby")).isEqualTo("HI TOBY");
        assertThat(proxiedHello.sayThankYou("Toby")).isEqualTo("THANK YOU TOBY");
    }
    
    private static class UppercaseAdvice implements MethodInterceptor {
        @Override
        public Object invoke(final MethodInvocation invocation) throws Throwable {
            final String ret = (String) invocation.proceed();
            
            return ret.toUpperCase();
        }
    }
}
