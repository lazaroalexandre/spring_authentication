package com.example.spring_authentication.api.dto.sendEmailDto;

public record SendEmailDto(
        String emailFrom,
        String emailTo,
        String title,
        String text) {}
