package com.example.spring_authentication.domain.messages;

public final class SendEmailMessage {
    public static final String linkValidate = "http://localhost:8080/usuario/validate/account/";

    public static final String successfullValidation = "Parabéns! Sua conta foi validada e você já pode fazer login no sistema.";

    public static final String titleWellcome = "Bem-vindo ao nosso sistema!";
    
    public static final String titleSuccessfullValidation = "Conta ativada com sucesso no nosso sistema!";
    
    public static final String titleDisableAccount = "Conta desativada no nosso sistema!";
    
    public static final String textWellcome(String id_user, String username) {
        return "Olá, "+username+"! Sua conta está quase concluída."+
                "\nClique no link abaixo para validar a sua conta:"+
                "\n"+linkValidate+id_user+
                "\n\nCaso tenha alguma dúvida, sinta-se a vontade para nos enviar."+
                "\n\nAtenciosamente,"+
                "\nSuporte Empresa.";
    }

    public static final String textSuccessfullValidation(String username) {
        return "Olá, "+username+"! Sua conta foi aceita e ativada no sistema."+
        "\nAgora você pode fazer login e desfrutar do que há de melhor no nosso sistema."+
        "\nCaso tenha alguma dúvida, sinta-se a vontade para nos enviar."+
        "\n\nAtenciosamente,"+
        "\nSuporte Empresa.";
    }

    public static final String textDisableAccount(String username) {
        return "Olá, "+username+"! Sua conta foi desativada no sistema."+
        "\nCaso tenha alguma dúvida, sinta-se a vontade para nos enviar."+
        "\n\nAtenciosamente,"+
        "\nSuporte Empresa.";
    }
}