package com.example.spring_authentication.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_authentication.config.MessagesSendEmailConfig;
import com.example.spring_authentication.models.EmailModel;
import com.example.spring_authentication.models.User;
import com.example.spring_authentication.repositories.UserRepository;
import com.example.spring_authentication.services.EmailService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/list")
    public List<User> getAllValidUsers() {
        return userRepository
                .findAll()
                .stream()
                .filter(user -> user.getValid())
                .collect(Collectors.toList());
    }

    @GetMapping("/list/invalid")
    public List<User> getAllInvalidUsers() {
        return userRepository
                .findAll()
                .stream()
                .filter(user -> !user.getValid())
                .collect(Collectors.toList());
    }

    @Autowired
    private EmailService emailService;

    @GetMapping("/detail/{userId}")
    public User getOneUser(@PathVariable String userId) {
        @SuppressWarnings("null")
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new RuntimeException("Usuário não existe!");
        }
    }

    @SuppressWarnings("null")
    @PatchMapping("/update/by-user/{userId}")
    public User updateUserByUser(@PathVariable String userId, @RequestBody User newUser) {
        Optional<User> user = userRepository.findById(userId);
        LocalDateTime updateTime = LocalDateTime.now();
        if (user.isPresent()) {
            User existingUser = user.get();
            if (newUser.getName() != null) {
                existingUser.setName(newUser.getName());
                existingUser.setUpdated(updateTime);
            }
            return userRepository.save(existingUser);
        } else {
            throw new RuntimeException("Usuário não existe!");
        }
    }

    @SuppressWarnings("null")
    @PatchMapping("/update/by-admin/{userId}")
    public User updateUserByAdmin(@PathVariable String userId, @RequestBody User newUser) {
        Optional<User> user = userRepository.findById(userId);
        LocalDateTime updateTime = LocalDateTime.now();
        if (user.isPresent()) {
            User existingUser = user.get();

            if (newUser.getRole() != null) {
                existingUser.setRole(newUser.getRole());
            }

            if (newUser.getValid() || !newUser.getValid()) {
                existingUser.setValid(newUser.getValid());
                MessagesSendEmailConfig message = new MessagesSendEmailConfig();
                if (newUser.getValid()) {
                    message.activatedAccountMessage(existingUser.getName());
                } else {
                    message.disabledAccountMessage(existingUser.getName());
                }
                EmailModel welcomeEmail = new EmailModel(message.getName(), existingUser.getEmail(),
                        message.getSubject(),
                        message.getText());
                emailService.sendEmail(welcomeEmail);
            }

            if (newUser.getRole() != null || (newUser.getValid() || !newUser.getValid())) {
                existingUser.setUpdated(updateTime);
            }
            return userRepository.save(existingUser);
        } else {
            throw new RuntimeException("Usuário não existe!");
        }
    }
}
