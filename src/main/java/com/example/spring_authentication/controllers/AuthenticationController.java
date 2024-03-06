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

import com.example.spring_authentication.dto.AuthenticateDto;
import com.example.spring_authentication.dto.LoginDto;
import com.example.spring_authentication.dto.RegistreDto;
import com.example.spring_authentication.models.SendEmail;
import com.example.spring_authentication.models.MessagesSendEmail;
import com.example.spring_authentication.models.UserModel;
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

        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManeger.authenticate(usernamePassword);
        var usuario = (UserModel) auth.getPrincipal();
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
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        UserModel newUser = new UserModel(data.name(), data.email(), encryptedPassword);

        userRepository.save(newUser);
        MessagesSendEmail message = new MessagesSendEmail();
        message.validateAccountMessage(
                "http://192.168.0.18:8080/auth/update/user/validate/" + newUser.getUsuario_id(),  newUser.getName());
        SendEmail welcomeEmail = new SendEmail(message.getName(), data.email(), message.getSubject(),
                message.getText());
        emailService.sendEmail(welcomeEmail);
        return ResponseEntity.ok().build();
    }

    @SuppressWarnings("null")
    @GetMapping("/update/user/validate/{userId}")
    public ResponseEntity<String> patchValidateUser(@PathVariable String userId) {
        LocalDateTime updateTime = LocalDateTime.now();
        UserModel existingUser = userRepository.findById(userId).get();
        if (existingUser.getValidByAdmin()) {
            
            existingUser.setValid(true);
            existingUser.setUpdated(updateTime);
            userRepository.save(existingUser);
            MessagesSendEmail message = new MessagesSendEmail();
            message.activatedAccountMessage(existingUser.getName());
            SendEmail welcomeEmail = new SendEmail(message.getName(), existingUser.getEmail(), message.getSubject(),
                    message.getText());
            emailService.sendEmail(welcomeEmail);
            return ResponseEntity.ok("Parabéns! Sua conta foi validada e você já pode fazer login no sistema.");
        
        }
        return ResponseEntity.ok("Deslcupe! Infelizmente sua conta foi desativada. Para mais informações, entre em contato com o suporte da empresa.");
    }
}