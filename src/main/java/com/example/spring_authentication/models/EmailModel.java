package com.example.spring_authentication.models;

import java.time.LocalDateTime;

import com.example.spring_authentication.enums.StatusEmail;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Table(name = "Email")
@Data
@Entity
public class EmailModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String email_id;
    @Column(nullable = true)
    private String ownerRef;
    @Column(nullable = false)
    private String emailFrom;
    @Column(nullable = false)
    private String emailTo;
    @Column(nullable = false)
    private String subject;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;
    private LocalDateTime sendDateEmail;
    private StatusEmail statusEmail;

    public EmailModel(String ownerRef, String emailTo, String subject, String text) {
        this.ownerRef = ownerRef;
        this.emailFrom = "{spring.mail.username}";
        this.emailTo = emailTo;
        this.subject = subject;
        this.text = text;
        this.sendDateEmail = LocalDateTime.now();
        this.statusEmail = StatusEmail.SEND;
    }
}
