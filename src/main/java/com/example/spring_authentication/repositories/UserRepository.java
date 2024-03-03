package com.example.spring_authentication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.example.spring_authentication.models.UserModel;

@CrossOrigin(origins = "*")
public interface UserRepository extends JpaRepository<UserModel, String> {
    UserDetails findByEmail(String email);
}