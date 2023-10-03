package com.masagreen.RentalUnitsManagement.utils;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SendMail {
    

    @Autowired
    private JavaMailSender emailSender;

    @Value("${mail.originmail}")
    private String originmail;
    
    public void sendPassword(
      String to, String subject) {
        String randomPassword = UUID.randomUUID().toString().substring(0, 4);
        log.info(randomPassword);
//        SimpleMailMessage message = new SimpleMailMessage();
//        // not mandatory however many servers require from prop
//        message.setFrom(originmail);
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText( randomPassword );
//        emailSender.send(message);
      
    }
    public void sendApprovedBy(String[] to, String subject, String content){
       
//        SimpleMailMessage message = new SimpleMailMessage();
//        // not mandatory however many servers require from prop
//        message.setFrom(originmail);
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText( content );
//        emailSender.send(message);
    }
}
    

