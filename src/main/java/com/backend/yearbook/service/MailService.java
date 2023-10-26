package com.backend.yearbook.service;

import com.backend.yearbook.model.MailModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
@Autowired
private JavaMailSender mailSender;
@Value("${spring.mail.username}")
private String fromMail;
    public void sendEmail(String mail, MailModel mailModel) {
        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
        simpleMailMessage.setFrom(fromMail);
        simpleMailMessage.setSubject(mailModel.getSubject());
        simpleMailMessage.setText(mailModel.getMessage());
        simpleMailMessage.setTo(mail);
        mailSender.send(simpleMailMessage);
    }
}
