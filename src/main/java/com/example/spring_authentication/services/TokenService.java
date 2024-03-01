package com.example.spring_authentication.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.example.spring_authentication.models.User;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.Instant;

@Service
public class TokenService {
    @Value("{api.security.token.secret}")
    private String secret;

    private Instant genExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getEmail())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
            return token;

        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar o token: ", exception);    
        }
    }
  
    public String validateToken(String token){
        Algorithm algorithm = Algorithm.HMAC256(secret);
        String tokenValidated = 
        JWT.require(algorithm)
            .withIssuer("auth-api")
            .build()
            .verify(token)
            .getSubject();
        return tokenValidated;
    }
}
