package springbook.user.service;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TransactionHandler implements InvocationHandler {
    private Object target;
    private PlatformTransactionManager transactionManager;
    private String pattern;
    
    public void setTarget(final Object target) {
        this.target = target;
    }
    
    public void setTransactionManager(final PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
    
    public void setPattern(final String pattern) {
        this.pattern = pattern;
    }
    
    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        if (method.getName().startsWith(this.pattern)) {
            return invokeTransaction(method, args);
        }
        
        return method.invoke(this.target, args);
    }
    
    private Object invokeTransaction(final Method method, final Object[] args) throws Throwable {
        final TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
        
        try {
            final Object ret = method.invoke(this.target, args);
            
            this.transactionManager.commit(status);
            
            return ret;
        } catch (final InvocationTargetException e) {
            this.transactionManager.rollback(status);
            
            throw e.getTargetException();
        }
    }
}
