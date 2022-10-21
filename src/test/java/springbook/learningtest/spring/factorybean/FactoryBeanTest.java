package springbook.learningtest.spring.factorybean;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { FactoryBeanTestConfig.class })
public class FactoryBeanTest {
    @Autowired
    ApplicationContext context;
    
    @DisplayName("팩토리빈 학습 테스트")
    @Test
    public void getMessageFromFactoryBean() {
        final Object message = this.context.getBean("message");
    
        assertThat(message).isInstanceOf(Message.class);
        assertThat(((Message) message).getText()).isEqualTo("Factory Bean");
    }
    
    @DisplayName("팩토리빈 자체를 가져오는 테스트")
    @Test
    public void getFactoryBean() {
        final Object factory = this.context.getBean("&message");
        
        assertThat(factory).isInstanceOf(MessageFactoryBean.class);
    }
}
