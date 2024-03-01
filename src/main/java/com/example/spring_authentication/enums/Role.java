package com.example.spring_authentication.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum Role {
    ADMIN("admin"),
    USER("user");

    private String role;
}
