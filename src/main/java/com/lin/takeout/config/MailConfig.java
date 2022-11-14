package com.lin.takeout.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import java.util.Properties;

@Configuration
public class MailConfig {

    @Value("${mail.host}")
    String host;
    @Value("${mail.username}")
    String username;
    @Value("${mail.password}")
    String password;
    @Bean
    public JavaMailSender javaMailSender(){
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        Properties mailProperties = new Properties();
        javaMailSender.setHost(host);
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);
        mailProperties.setProperty("mail.smtp.auth","true");
        mailProperties.setProperty("mail.smtp.timeout","25000");
        javaMailSender.setJavaMailProperties(mailProperties);
        return javaMailSender;
    }
}
