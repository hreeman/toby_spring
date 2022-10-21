package springbook.learningtest.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class UppercaseHandler implements InvocationHandler {
    Object target;
    
    public UppercaseHandler(final Object target) {
        this.target = target;
    }
    
    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        final Object ret = method.invoke(this.target, args);
        
        if (ret instanceof String) {
            return ((String) ret).toUpperCase();
        }
        
        return ret;
    }
}
