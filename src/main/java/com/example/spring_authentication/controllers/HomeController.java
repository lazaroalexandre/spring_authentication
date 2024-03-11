package com.example.spring_authentication.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_authentication.enums.Role;
import com.example.spring_authentication.models.UserModel;
import com.example.spring_authentication.repositories.UserRepository;

@RestController
@RequestMapping("/")
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @SuppressWarnings({ "rawtypes", "null" })
    @GetMapping("/home/{userId}")
    public ResponseEntity home(@PathVariable String userId) {
        Optional<UserModel> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            UserModel user = userOptional.get();
            if (user.getRole() == Role.USER) {
                return ResponseEntity.status(HttpStatus.OK).body("Bem-vindo, " + user.getName() + "!"+
                "\n" + "Você tem permissão de USER");
            } else if (user.getRole() == Role.ADMIN) {
                return ResponseEntity.status(HttpStatus.OK).body("Bem-vindo, " + user.getName() + "!" +
                "\n" + "Você tem permissão de ADMIN");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bem-vindo!\nVocê precisa se registrar ou fazer login para entrar no sistema.");
    }
}
