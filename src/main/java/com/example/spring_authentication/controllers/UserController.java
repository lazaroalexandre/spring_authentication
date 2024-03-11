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

import com.example.spring_authentication.models.SendEmail;
import com.example.spring_authentication.enums.Role;
import com.example.spring_authentication.models.MessagesSendEmail;
import com.example.spring_authentication.models.UserModel;
import com.example.spring_authentication.repositories.UserRepository;
import com.example.spring_authentication.services.EmailService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/list")
    public List<UserModel> getAllValidUsers() {
        return userRepository
                .findAll()
                .stream()
                .filter(user -> user.getValid())
                .collect(Collectors.toList());
    }

    @GetMapping("/list/invalid")
    public List<UserModel> getAllInvalidUsers() {
        return userRepository
                .findAll()
                .stream()
                .filter(user -> !user.getValid())
                .collect(Collectors.toList());
    }

    @GetMapping("/list/valid/by-admin")
    public List<UserModel> getAllValidUsersByAdmin() {
        return userRepository
                .findAll()
                .stream()
                .filter(user -> user.getValidByAdmin())
                .collect(Collectors.toList());
    }

    @GetMapping("/list/invalid/by-admin")
    public List<UserModel> getAllInvalidUsersByAdmin() {
        return userRepository
                .findAll()
                .stream()
                .filter(user -> !user.getValidByAdmin())
                .collect(Collectors.toList());
    }

    @Autowired
    private EmailService emailService;

    @GetMapping("/detail/{userId}")
    public UserModel getOneUser(@PathVariable String userId) {
        @SuppressWarnings("null")
        Optional<UserModel> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new RuntimeException("Usuário não existe!");
        }
    }

    @SuppressWarnings("null")
    @PatchMapping("/update/by-user/{userId}")
    public UserModel updateUserByUser(@PathVariable String userId, @RequestBody UserModel newUser) {
        Optional<UserModel> user = userRepository.findById(userId);
        LocalDateTime updateTime = LocalDateTime.now();
        if (user.isPresent()) {
            UserModel existingUser = user.get();
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
    public UserModel updateUserByAdmin(@PathVariable String userId, @RequestBody UserModel newUser) {
        Optional<UserModel> user = userRepository.findById(userId);
        LocalDateTime updateTime = LocalDateTime.now();
        if (user.isPresent()) {
            UserModel existingUser = user.get();

            if (newUser.getRole() != null) {
                existingUser.setRole(newUser.getRole());
            }

            if (newUser.getValidByAdmin() || !newUser.getValidByAdmin()) {
                MessagesSendEmail message = new MessagesSendEmail();
                if (newUser.getValidByAdmin()) {
                    existingUser.setValidByAdmin(true);
                    existingUser.setValid(true);
                    message.activatedAccountMessage(existingUser.getName());
                } else {
                    existingUser.setValidByAdmin(false);
                    existingUser.setValid(false);
                    message.disabledAccountMessage(existingUser.getName());
                }
                SendEmail welcomeEmail = new SendEmail(message.getName(), existingUser.getEmail(),
                        message.getSubject(),
                        message.getText());
                emailService.sendEmail(welcomeEmail);
            }

            if (newUser.getRole() != null || (newUser.getValidByAdmin() || !newUser.getValidByAdmin())) {
                existingUser.setUpdated(updateTime);
            }
            return userRepository.save(existingUser);
        } else {
            throw new RuntimeException("Usuário não existe!");
        }
    }

    @SuppressWarnings("null")
    @PatchMapping("/update/invalid/by-user/{userId}")
    public UserModel updateCancelAccountByUser(@PathVariable String userId) {
        Optional<UserModel> user = userRepository.findById(userId);
        LocalDateTime updateTime = LocalDateTime.now();
        MessagesSendEmail message = new MessagesSendEmail();
        if (user.isPresent()) {
            UserModel existingUser = user.get();
            if (existingUser.getRole() == Role.USER) {         
                existingUser.setValid(false);
                existingUser.setUpdated(updateTime);
                existingUser.setValidByAdmin(false);
                message.disabledAccountMessage(existingUser.getName());
                SendEmail welcomeEmail = new SendEmail(message.getName(), existingUser.getEmail(),
                        message.getSubject(),
                        message.getText());
                emailService.sendEmail(welcomeEmail);
                return userRepository.save(existingUser);
            }
            throw new RuntimeException("Permissão negada!"); 
        } else {
            throw new RuntimeException("Usuário não existe!");
        }
    }
}
