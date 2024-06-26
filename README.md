# Spring Authentication

    Aplicação web de registro e login de usuários, usando o jwt e confirmação de e-mails do google.
    Existe 2(duas) branches, além dessa main, que correspondem a diferentes formas de autencação de usuário.
    A branch chamada "feat/auth-with-password" corresponde ao login de usuário com e-mail e senha.
    Já a branch chamada "feat/auth-without-password" corresponde ao login de usuário com apenas e-mail, com finalidade de fazer login via oauth do google.
    
# Tecnologias usadas

    Spring - Java
    Postgres - SQL

# Configurações em localhost

    - Instale o Maven na sua máquina;
    - Instale o Postgres SQL na sua máquina;
    - Crie um usuário chamado postgres no Postgres SQL;
    - Altere a senha de usuário para postgres também;
    - Crie um banco de dados chamado "spring-auth";
    - No arquivo "aplication.properties", altere o valor de "JWT_SECRET" na chave secreta para qualquer palavra que você achar secreta;
    - No arquivo "application.properties", altere o seu username e password nas confirgurações de smtp. Veja como adicionar a senha correta do seu e-mail de envio escolhido: https://support.google.com/accounts/answer/185833


# Dependências implantadas

    Spring Web
    Spring Data JPA
    Lombok
    Spring Boot DevTools
    PostgreSQL Driver
    Spring Security
    Java Mail Sender
    Validation
    Java JWT


# Execução

    [LINUX] Execute o comando para limpar e reconstruir o projeto spring no linux:

    mvn clean package
    
    [LINUX] Execute o comando para rodar o projeto no linux:

    mvn spring-boot:run

    -----------------------------------------------------------------------------------

    [WINDOWS] Execute o comando para limpar e reconstruir o projeto spring no windows:
    
    ./mvnw clean package
    
    [WINDOWS] Execute o comando para rodar o projeto no windows:

    ./mvnw spring-boot:run

# Endpoints fora do e-mail

- ## Jsons para a branche feat/auth-without-password

    endpoints-insominia/auth-without-password&confirm-email.json

- ## Jsons para a branche feat/auth-with-password

    endpoints-insominia/auth-with-password&confirm-email.json


# Diagrama de Caso de Uso

![alt](images/Diagrama%20de%20Caso%20de%20Uso.png)

# Modelo Lógico de Banco de dados

![alt](images/Diagrama%20Láogico%20de%20Banco%20de%20Dados.png)

