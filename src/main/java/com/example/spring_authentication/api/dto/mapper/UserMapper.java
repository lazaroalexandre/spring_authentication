package com.example.spring_authentication.api.dto.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.example.spring_authentication.api.dto.userDto.UserDetailDto;
import com.example.spring_authentication.api.dto.userDto.UserSaveDto;
import com.example.spring_authentication.domain.models.User;

@Component
public class UserMapper {
    public UserSaveDto toDto(User user) {
        if (user == null) {
            return null;
        }
        return new UserSaveDto(user.getId(), user.getName(), user.getEmail(), user.getRole(), user.getValid(),
                user.getValidByAdmin());
    }

    public UserDetailDto toDtoDetail(User user){
        if (user == null) {
            return null;
        }
        return new UserDetailDto(user.getId(), user.getName(), user.getEmail(), user.getRole(), user.getCreated(), user.getUpdated());
    }

    public User toEntity(UserSaveDto userDto) {
        if (userDto == null) {
            return null;
        }

        User user = new User();

        if (userDto.id() != null) {
            user.setId(userDto.id());
        }
        user.setName(userDto.name());
        user.setEmail(userDto.email());
        user.setRole(userDto.role());
        user.setValid(userDto.valid());
        user.setValidByAdmin(userDto.validByAdmin());
        user.setUpdated(LocalDateTime.now());

        return user;
    }

}
