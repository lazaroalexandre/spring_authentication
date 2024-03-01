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
    private AuthenticationManager authenticationManager;

    @Autowired
    private EmailService emailService;

    @SuppressWarnings("rawtypes")
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Validated AuthenticateDto data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = authenticationManager.authenticate(usernamePassword);
        var user = (User) auth.getPrincipal();
        var token = tokenService.generateToken(user);

        if (user != null && user.getValid() != false) {
            return ResponseEntity.ok(new LoginDto(token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Autenticação inválida!");
        }
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Validated RegistreDto data) {
        if (userRepository.findByEmail(data.email()) != null) {
            return ResponseEntity.badRequest().build();
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User(data.name(), data.email(), encryptedPassword);

        userRepository.save(newUser);
        MessagesSendEmailConfig message = new MessagesSendEmailConfig();
        System.out.println(newUser.getName() + newUser.getEmail());
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
        return ResponseEntity.ok("Parabéns! Sua conta foi validada e você já pode fazer login no sistema.");
    }
}