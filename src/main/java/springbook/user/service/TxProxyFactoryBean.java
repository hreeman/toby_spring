package springbook.user.service;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.Proxy;

public class TxProxyFactoryBean implements FactoryBean<Object> {
    Object target;
    PlatformTransactionManager transactionManager;
    String pattern;
    Class<?> serviceInterface;
    
    public void setTarget(final Object target) {
        this.target = target;
    }
    
    public void setTransactionManager(final PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
    
    public void setPattern(final String pattern) {
        this.pattern = pattern;
    }
    
    public void setServiceInterface(final Class<?> serviceInterface) {
        this.serviceInterface = serviceInterface;
    }
    
    @Override
    public Object getObject() throws Exception {
        final TransactionHandler txHandler = new TransactionHandler();
        
        txHandler.setTarget(this.target);
        txHandler.setTransactionManager(this.transactionManager);
        txHandler.setPattern(this.pattern);
        
        return Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{ this.serviceInterface },
                txHandler
        );
    }
    
    @Override
    public Class<?> getObjectType() {
        return this.serviceInterface;
    }
    
    @Override
    public boolean isSingleton() {
        return false;
    }
}
