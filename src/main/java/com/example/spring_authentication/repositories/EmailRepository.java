package com.example.spring_authentication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.example.spring_authentication.models.SendEmail;

@CrossOrigin(origins = "*")
public interface EmailRepository extends JpaRepository<SendEmail, String>{
    
}
