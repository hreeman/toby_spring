package springbook.user.service;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

/**
 * 테스트를 위한 아무런 기능이 없는 MailSender 구현체
 */
public class DummyMailSender implements MailSender {
    @Override
    public void send(final SimpleMailMessage simpleMessage) throws MailException {
    
    }
    
    @Override
    public void send(final SimpleMailMessage... simpleMessages) throws MailException {
    
    }
}
