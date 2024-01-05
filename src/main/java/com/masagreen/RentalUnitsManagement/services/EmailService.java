package com.masagreen.RentalUnitsManagement.services;

import com.masagreen.RentalUnitsManagement.models.entities.AppUser;
import com.masagreen.RentalUnitsManagement.utils.Constants;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {


    @Value("${mail.origin}")
    private String origin;
    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;
    public void sendEmailValidationCode(String email, String validationCode){
        Context context = new Context();
        String validationUrl = Constants.BASE_URL+"auth/validate-email/"+validationCode;
        String resendUrl = Constants.BASE_URL+"auth/resend-email-code/"+email;
        context.setVariable("validationUrl", validationUrl);
        context.setVariable("resendUrl", resendUrl );
        String content = templateEngine.process("", context);

        try{
            log.info("sending validationcode to {}", email);
            sendMailContent(content, email, "Email Verification");
        }catch (MessagingException ex){
            throw new RuntimeException(ex);
        }
    }
    //candidate for async
    public void sendApprovedByEmail(List<AppUser> admins, String email, boolean status){
        Context context = new Context();
        String subject;
        if(status){
            subject="User Approved";
        }else{
            subject="User approval revoked";
        }
        context.setVariable("user-email", email);
        String content = templateEngine.process("",context);
        for(AppUser admin: admins){
           try{
               log.info("sending approval/disapproval mail to {} ", admin.getEmail());
               sendMailContent(content, admin.getEmail(), subject);
           }catch(MessagingException ex){
               throw new RuntimeException(ex);
           }

        }
    }

    public void sendPassword(
            String to, String subject) {
        String randomPassword = UUID.randomUUID().toString().substring(0, 4);
        log.info(randomPassword);
        //make html template


    }

    private void sendMailContent(String content, String email, String subject) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(origin);
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(content, true);
        javaMailSender.send(message);
    }
}
    

