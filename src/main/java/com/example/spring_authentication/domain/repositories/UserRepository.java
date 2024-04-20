package com.example.spring_authentication.domain.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.example.spring_authentication.domain.models.User;

import java.util.Optional;

@CrossOrigin(origins = "*")
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    Page<User> findByValidTrue(Pageable pageable);
    Page<User> findByValidFalse(Pageable pageable);
    Page<User> findByValidByAdminFalse(Pageable pageable);   
}
