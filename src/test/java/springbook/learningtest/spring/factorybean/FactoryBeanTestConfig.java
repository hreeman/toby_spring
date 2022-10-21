package springbook.learningtest.spring.factorybean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FactoryBeanTestConfig {
    @Bean(name = "message")
    public MessageFactoryBean messageFactoryBean() {
        final MessageFactoryBean messageFactoryBean = new MessageFactoryBean();
    
        messageFactoryBean.setText("Factory Bean");
    
        return messageFactoryBean;
    }
    
    @Bean
    public Message message() throws Exception {
        return this.messageFactoryBean().getObject();
    }
}
