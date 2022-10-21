package springbook.learningtest.jdk;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("6장 리플렉션, 프록시 학습 테스트")
public class ReflectionTest {
    @DisplayName("리플렉션 학습 테스트")
    @Test
    public void invokeMethod() throws Exception {
        final String name = "Spring";
    
        // length
        assertThat(name.length()).isEqualTo(6);
        
        final Method lengthMethod = String.class.getMethod("length");
        assertThat((Integer) lengthMethod.invoke(name)).isEqualTo(6);
        
        // charAt
        assertThat(name.charAt(0)).isEqualTo('S');
        
        final Method charAtMethod = String.class.getMethod("charAt", int.class);
        assertThat((Character) charAtMethod.invoke(name, 0)).isEqualTo('S');
    }
    
    @DisplayName("클라이언트 역할 테스트")
    @Test
    public void simpleProxy() {
        final Hello hello = new HelloTarget();
        
        assertThat(hello.sayHello("Toby")).isEqualTo("Hello Toby");
        assertThat(hello.sayHi("Toby")).isEqualTo("Hi Toby");
        assertThat(hello.sayThankYou("Toby")).isEqualTo("Thank You Toby");
    }
    
    @DisplayName("HelloUppercase 프록시 테스트")
    @Test
    public void HelloUppercaseProxy() {
        final Hello proxiedHello = new HelloUppercase(new HelloTarget());
        
        assertThat(proxiedHello.sayHello("Toby")).isEqualTo("HELLO TOBY");
        assertThat(proxiedHello.sayHi("Toby")).isEqualTo("HI TOBY");
        assertThat(proxiedHello.sayThankYou("Toby")).isEqualTo("THANK YOU TOBY");
    }
    
    @DisplayName("HelloUppercase InvokeHandler 프록시 테스트")
    @Test
    public void HelloUppercaseHandlerProxy() {
        final Hello proxiedHello = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[] { Hello.class },
                new UppercaseHandler(new HelloTarget())
        );
    
        assertThat(proxiedHello.sayHello("Toby")).isEqualTo("HELLO TOBY");
        assertThat(proxiedHello.sayHi("Toby")).isEqualTo("HI TOBY");
        assertThat(proxiedHello.sayThankYou("Toby")).isEqualTo("THANK YOU TOBY");
    }
}
