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

import com.example.spring_authentication.models.User;
import com.example.spring_authentication.repositories.UserRepository;

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
    @PatchMapping("/update/{userId}")
    public User updateUser(@PathVariable String userId, @RequestBody User newUser) {
        Optional<User> user = userRepository.findById(userId);
        LocalDateTime updateTime = LocalDateTime.now();
        if (user.isPresent()) {
            User existingUser = user.get();
            if (newUser.getPassword() != null) {
                existingUser.setPassword(newUser.getPassword());
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
                existingUser.setUpdated(updateTime);
            }
            return userRepository.save(existingUser);
        } else {
            throw new RuntimeException("Usuário não existe!");
        }
    }
}
