package com.example.spring_authentication.domain.services;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.example.spring_authentication.api.dto.userDto.UserSaveDto;
import com.example.spring_authentication.domain.exception.DomainException;
import com.example.spring_authentication.domain.messages.ExceptionMessage;

@Service
public class TokenUserService {
    @Value("{api.security.token.secret}")
    private String secret;

    private Instant genExpirationDate() {
        return LocalDateTime.now().plusHours(12).toInstant(ZoneOffset.of("-03:00"));
    }

    public String generateToken(UserSaveDto userDto) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(userDto.email())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
            return token;
        } catch (IllegalArgumentException e) {
            throw new DomainException(ExceptionMessage.token(e.toString()));
        } catch (JWTCreationException e) {
            throw new DomainException(ExceptionMessage.token(e.toString()));
        }
    }

    public String validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        String tokenValidated = JWT.require(algorithm)
                .withIssuer("auth-api")
                .build()
                .verify(token)
                .getSubject();
        return tokenValidated;
    }

}
