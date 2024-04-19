package com.example.spring_authentication.domain.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.spring_authentication.api.dto.mapper.UserMapper;
import com.example.spring_authentication.api.dto.userDto.UserDetailDto;
import com.example.spring_authentication.api.dto.userDto.UserPageDto;
import com.example.spring_authentication.api.dto.userDto.UserSaveDto;
import com.example.spring_authentication.api.dto.userDto.UserTokenDto;
import com.example.spring_authentication.domain.enums.Role;
import com.example.spring_authentication.domain.exception.DomainException;
import com.example.spring_authentication.domain.messages.ExceptionMessage;
import com.example.spring_authentication.domain.messages.SendEmailMessage;
import com.example.spring_authentication.domain.repositories.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private UserMapper userMapper;
    private TokenUserService tokenService;
    private SendEmailService sendEmailService;

    @Transactional
    public UserPageDto listValid(int page, int pageSize) {
        Page<UserDetailDto> userPage = userRepository.findByValidTrue(PageRequest.of(page, pageSize))
                .map(user -> userMapper.toDtoDetail(user));
        return new UserPageDto(userPage.getContent(), userPage.getTotalElements(), userPage.getTotalPages());
    }

    @Transactional
    public UserPageDto listInvalid(int page, int pageSize) {
        Page<UserDetailDto> userPage = userRepository.findByValidFalse(PageRequest.of(page, pageSize))
                .map(user -> userMapper.toDtoDetail(user));
        return new UserPageDto(userPage.getContent(), userPage.getTotalElements(), userPage.getTotalPages());
    }

    @Transactional
    public UserPageDto listInvalidByAdmin(int page, int pageSize) {
        Page<UserDetailDto> userPage = userRepository.findByValidByAdminFalse(PageRequest.of(page, pageSize))
                .map(user -> userMapper.toDtoDetail(user));
        return new UserPageDto(userPage.getContent(), userPage.getTotalElements(), userPage.getTotalPages());
    }

    @Transactional
    public UserDetailDto detail(String id) {
        return userRepository.findById(id).map(user -> userMapper.toDtoDetail(user))
                .orElseThrow(() -> new DomainException(ExceptionMessage.notFound("Usuário")));
    }

    @Transactional
    public UserSaveDto save(UserSaveDto userDto) {
        boolean emailUsed = userRepository.findByEmail(userMapper.toEntity(userDto).getEmail()).isPresent();
        if (emailUsed) {
            throw new DomainException(ExceptionMessage.attributeUsed("E-mail"));
        }
        UserSaveDto newUserDto = new UserSaveDto(userDto.id(), userDto.name(), userDto.email(), Role.USER, false, true);
        UserSaveDto saveUser = userMapper.toDto(userRepository.save(userMapper.toEntity(newUserDto)));
        sendEmailService.sendWellcomeAccount(saveUser);
        return saveUser;

    }

    @Transactional
    public UserTokenDto login(UserSaveDto userDto) {
        Optional<UserSaveDto> user = userRepository.findByEmail(userMapper.toEntity(userDto).getEmail())
                .map(u -> userMapper.toDto(u));
        if (user == null || user.get().valid() == false) {
            throw new DomainException(ExceptionMessage.invalidAuthentication);
        }
        String token = tokenService.generateToken(user.get());
        return new UserTokenDto(token);
    }

    @Transactional
    public UserSaveDto updateByUser(String id, UserSaveDto saveUserDto) {
        return userRepository.findById(id).map(recordFound -> {
            if (recordFound.getRole() == Role.USER) {
                if (saveUserDto.name() != null) {
                    recordFound.setName(saveUserDto.name());
                }
                if (saveUserDto.valid() != null && saveUserDto.valid() == false && recordFound.getValid() == true) {
                    recordFound.setValid(saveUserDto.valid());
                }
                return userRepository.save(recordFound);
            }
            throw new DomainException(ExceptionMessage.notFound("ADMIN"));
        }).map(user -> userMapper.toDto(user))
                .orElseThrow(() -> new DomainException(ExceptionMessage.notFound("Usuário")));
    }

    @Transactional
    public String validateAccount(String id) {
        UserSaveDto userSaveDto = userRepository.findById(id).map(recordFound -> {
            if (recordFound.getValidByAdmin()) {
                recordFound.setValid(true);
            }
            return userRepository.save(recordFound);
        }).map(user -> userMapper.toDto(user))
                .orElseThrow(() -> new DomainException(ExceptionMessage.notFound("Usuário")));
        if (userSaveDto.valid()) {
            sendEmailService.sendValidationAccount(userSaveDto);
            return SendEmailMessage.successfullValidation;
        }
        return ExceptionMessage.unsuccessfullValidation;
    }

    @Transactional
    public UserSaveDto suspendAccount(String id) {
        return userRepository.findById(id).map(recordFound -> {
            recordFound.setValid(false);
            recordFound.setValidByAdmin(false);
            sendEmailService.sendDisableAccount(userMapper.toDto(recordFound));
            return userRepository.save(recordFound);
        }).map(user -> userMapper.toDto(user))
                .orElseThrow(() -> new DomainException(ExceptionMessage.notFound("Usuário")));
    }

    @Transactional
    public UserSaveDto reactivateAccount(String id) {
        return userRepository.findById(id).map(recordFound -> {
            recordFound.setValid(true);
            recordFound.setValidByAdmin(true);
            sendEmailService.sendValidationAccount(userMapper.toDto(recordFound));
            return userRepository.save(recordFound);
        }).map(user -> userMapper.toDto(user))
                .orElseThrow(() -> new DomainException(ExceptionMessage.notFound("Usuário")));
    }

    @Transactional
    public UserSaveDto changeRole(String id, UserSaveDto saveUserDto) {
        return userRepository.findById(id).map(recordFound -> {
            if (saveUserDto.role() != null) {
                recordFound.setRole(saveUserDto.role());
            }
            return userRepository.save(recordFound);
        }).map(user -> userMapper.toDto(user))
                .orElseThrow(() -> new DomainException(ExceptionMessage.notFound("Usuário")));
    }
}
