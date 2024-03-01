package com.example.spring_authentication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.example.spring_authentication.models.EmailModel;

@CrossOrigin(origins = "*")
public interface EmailRepository extends JpaRepository<EmailModel, String>{
    
}
