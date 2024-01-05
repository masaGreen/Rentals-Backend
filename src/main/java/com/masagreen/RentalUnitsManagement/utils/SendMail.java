package com.masagreen.RentalUnitsManagement.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class SendMail {


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

    public void sendApprovedBy(String[] to, String subject, String content) {

//        SimpleMailMessage message = new SimpleMailMessage();
//        // not mandatory however many servers require from prop
//        message.setFrom(originmail);
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText( content );
//        emailSender.send(message);
    }
}
    

