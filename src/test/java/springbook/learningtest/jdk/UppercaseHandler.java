package springbook.learningtest.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class UppercaseHandler implements InvocationHandler {
    Hello target;
    
    public UppercaseHandler(final Hello target) {
        this.target = target;
    }
    
    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        final String ret = (String) method.invoke(this.target, args);
        
        return ret.toUpperCase();
    }
}
