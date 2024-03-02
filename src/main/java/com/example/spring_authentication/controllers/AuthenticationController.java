package com.example.spring_authentication.controllers;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_authentication.config.MessagesSendEmailConfig;
import com.example.spring_authentication.dto.AuthenticateDto;
import com.example.spring_authentication.dto.LoginDto;
import com.example.spring_authentication.dto.RegistreDto;
import com.example.spring_authentication.models.EmailModel;
import com.example.spring_authentication.models.User;
import com.example.spring_authentication.repositories.UserRepository;
import com.example.spring_authentication.services.EmailService;
import com.example.spring_authentication.services.TokenService;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManeger;

    @Autowired
    private EmailService emailService;

    @SuppressWarnings("rawtypes")
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Validated AuthenticateDto data) {

        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.email());
        var auth = this.authenticationManeger.authenticate(usernamePassword);
        var usuario = (User) auth.getPrincipal();
        var token = tokenService.generateToken(usuario);

        if (usuario == null || usuario.getValid() == false) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Autenticação Inválida");
        } else {
            return ResponseEntity.ok(new LoginDto(token));
        }
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Validated RegistreDto data) {
        if (userRepository.findByEmail(data.email()) != null) {
            return ResponseEntity.badRequest().build();
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.email());
        User newUser = new User(data.name(), data.email(), encryptedPassword);

        userRepository.save(newUser);
        MessagesSendEmailConfig message = new MessagesSendEmailConfig();
        message.validateAccountMessage(
                "http://192.168.0.18:8080/auth/update/user/validate/" + newUser.getUsuario_id(),  newUser.getName());
        EmailModel welcomeEmail = new EmailModel(message.getName(), data.email(), message.getSubject(),
                message.getText());
        emailService.sendEmail(welcomeEmail);
        return ResponseEntity.ok().build();
    }

    @SuppressWarnings("null")
    @GetMapping("/update/user/validate/{userId}")
    public ResponseEntity<String> patchValidateUser(@PathVariable String userId) {
        LocalDateTime updateTime = LocalDateTime.now();
        User existingUser = userRepository.findById(userId).get();
        existingUser.setValid(true);
        existingUser.setUpdated(updateTime);
        userRepository.save(existingUser);
        MessagesSendEmailConfig message = new MessagesSendEmailConfig();
        message.activatedAccountMessage(existingUser.getName());
        EmailModel welcomeEmail = new EmailModel(message.getName(), existingUser.getEmail(), message.getSubject(),
                message.getText());
        emailService.sendEmail(welcomeEmail);
        return ResponseEntity.ok("Parabéns! Sua conta foi validada e você já pode fazer login no sistema.");
    }
}