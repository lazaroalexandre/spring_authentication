package com.example.spring_authentication.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessagesSendEmailConfig {
    private String subject;
    private String name;
    private String text;
    private String link;

    public String validateAccountMessage(String link, String name) {
        this.subject = "Bem-vindo ao nosso sistema!";
        this.link = link;
        this.name = name;
        this.text = "Olá, " + this.name + "! Sua conta está quase concluída." +
                "\nClique no link abaixo para validar a sua conta:"+
                "\n" + this.link;
        return this.text;
    }
}
