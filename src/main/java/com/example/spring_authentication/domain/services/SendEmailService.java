package com.example.spring_authentication.domain.services;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.spring_authentication.api.dto.sendEmailDto.SendEmailDto;
import com.example.spring_authentication.api.dto.userDto.UserSaveDto;
import com.example.spring_authentication.domain.exception.DomainException;
import com.example.spring_authentication.domain.messages.SendEmailMessage;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SendEmailService {
    private JavaMailSender emailSender;

    public void sendEmail(SendEmailDto sendEmailDto) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(sendEmailDto.emailFrom());
            message.setTo(sendEmailDto.emailTo());
            message.setSubject(sendEmailDto.title());
            message.setText(sendEmailDto.text());
            emailSender.send(message);
        } catch (MailException e) {
            throw new DomainException(e.toString());
        }
    }

    @Transactional
    public void sendWellcomeAccount(UserSaveDto userSaveDto) {
        SendEmailDto sendEmailDto = new SendEmailDto("{spring.mail.username}", userSaveDto.email(),
                SendEmailMessage.titleWellcome, SendEmailMessage.textWellcome(userSaveDto.id(), userSaveDto.name()));
        sendEmail(sendEmailDto);
    }

    @Transactional
    public void sendValidationAccount(UserSaveDto userSaveDto) {
        SendEmailDto sendEmailDto = new SendEmailDto("{spring.mail.username}", userSaveDto.email(),
                SendEmailMessage.titleSuccessfullValidation, SendEmailMessage.textSuccessfullValidation(userSaveDto.name()));
        sendEmail(sendEmailDto);
    }

    @Transactional
    public void sendDisableAccount(UserSaveDto userSaveDto) {
        SendEmailDto sendEmailDto = new SendEmailDto("{spring.mail.username}", userSaveDto.email(),
                SendEmailMessage.titleDisableAccount, SendEmailMessage.textDisableAccount(userSaveDto.name()));
        sendEmail(sendEmailDto);
    }

}
