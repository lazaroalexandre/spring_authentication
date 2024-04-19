package com.example.spring_authentication.api.dto.userDto;

import java.time.LocalDateTime;

import com.example.spring_authentication.domain.enums.Role;

public record UserDetailDto(
                String id,
                String name,
                String email,
                Role role,
                LocalDateTime created,
                LocalDateTime updated) {

}
