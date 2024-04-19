package com.example.spring_authentication.api.dto.userDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.example.spring_authentication.domain.enums.Role;
import com.example.spring_authentication.domain.validation.GroupValidation;

public record UserSaveDto(
                String id,
                @NotBlank(groups = GroupValidation.Create.class) @Size(max = 255) String name,
                @NotBlank(groups = {
                                GroupValidation.Create.class,
                                GroupValidation.Login.class }) @Size(max = 255) @Email String email,
                Role role,
                Boolean valid,
                Boolean validByAdmin){

        public Collection<? extends GrantedAuthority> getAuthorities() {

                if (this.role == Role.ADMIN) {
                        return List.of(
                                        new SimpleGrantedAuthority("ROLE_ADMIN"),
                                        new SimpleGrantedAuthority("ROLE_USER"));
                } else if (this.role == Role.USER) {
                        return List.of(
                                        new SimpleGrantedAuthority("ROLE_USER"));
                } else {
                        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
                }
        }

}