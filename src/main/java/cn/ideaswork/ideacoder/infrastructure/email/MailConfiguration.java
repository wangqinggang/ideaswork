package cn.ideaswork.ideacoder.infrastructure.email;


import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSenderImpl;

//@Configuration
public class MailConfiguration {
    @Bean
    public JavaMailSenderImpl JavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.qq.com");
        mailSender.setUsername("xxxxxxx@qq.com");
        mailSender.setPassword("xxxxxxx");
        return mailSender;
    }
}