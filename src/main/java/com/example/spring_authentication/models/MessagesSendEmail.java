package com.example.spring_authentication.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessagesSendEmail {
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
                "\n" + this.link+
                "\n\nCaso tenha alguma dúvida, sinta-se a vontade para nos enviar."+
                "\n\nAtenciosamente,"+
                "\nSuporte Empresa.";
        return this.text;
    }
    
    public String activatedAccountMessage(String name) {
        this.subject = "Conta ativada no sistema!";
        this.name = name;
        this.text = "Olá, " + this.name + "! Sua conta foi aceita e ativada no sistema." +
                "\nAgora você pode fazer login e desfrutar do que há de melhor no nosso sistema."+
                "\nCaso tenha alguma dúvida, sinta-se a vontade para nos enviar."+
                "\n\nAtenciosamente,"+
                "\nSuporte Empresa.";
        return this.text;
    }

    public String disabledAccountMessage(String name) {
        this.subject = "Conta desativada no sistema!";
        this.name = name;
        this.text = "Olá, " + this.name + "! Sua conta foi desativada no sistema." +
                "\nCaso tenha alguma dúvida, sinta-se a vontade para nos enviar."+
                "\n\nAtenciosamente,"+
                "\nSuporte Empresa.";
        return this.text;
    }
}
