package com.example.spring_authentication.api.controllers;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_authentication.api.dto.userDto.UserDetailDto;
import com.example.spring_authentication.api.dto.userDto.UserPageDto;
import com.example.spring_authentication.api.dto.userDto.UserSaveDto;
import com.example.spring_authentication.api.dto.userDto.UserTokenDto;
import com.example.spring_authentication.domain.services.UserService;
import com.example.spring_authentication.domain.validation.GroupValidation;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/usuario")
public class UserController {
    private UserService userService;

    @GetMapping("/list/valid")
    public UserPageDto findByAllUsersValid(@RequestParam(defaultValue = "0") @PositiveOrZero int page, @RequestParam(defaultValue = "10") @Positive int pageSize){
        return userService.listValid(page, pageSize);
    }

    @GetMapping("/list/invalid")
    public UserPageDto findByAllUsersInvalid(@RequestParam(defaultValue = "0") @PositiveOrZero int page, @RequestParam(defaultValue = "10") @Positive int pageSize){
        return userService.listInvalid(page, pageSize);
    }

    @GetMapping("/list/admin/invalid")
    public UserPageDto findByAllUsersInvalidByAdmin(@RequestParam(defaultValue = "0") @PositiveOrZero int page, @RequestParam(defaultValue = "10") @Positive int pageSize){
        return userService.listInvalidByAdmin(page, pageSize);
    }

    @GetMapping("/detail/{id}")
    public UserDetailDto findUserById(@PathVariable String id){
        return userService.detail(id);
    }

    @GetMapping("/validate/account/{id}")
    public String validateAccount(@PathVariable String id){
        return userService.validateAccount(id);
    }

    @PatchMapping("/update/user/{id}")
    public UserSaveDto updateUser(@PathVariable String id, @RequestBody UserSaveDto saveUserDto){
        return userService.updateByUser(id, saveUserDto);
    }

    @PatchMapping("/suspense/account/{id}")
    public UserSaveDto suspenseAccount(@PathVariable String id){
        return userService.suspendAccount(id);
    }

    @PatchMapping("/reactivate/account/{id}")
    public UserSaveDto reactivateAccount(@PathVariable String id){
        return userService.reactivateAccount(id);
    }

    @PatchMapping("/update/role/{id}")
    public UserSaveDto updateRole(@PathVariable String id, @RequestBody UserSaveDto saveUserDto){
        return userService.changeRole(id, saveUserDto);
    }

    @PostMapping("/register")
    public UserSaveDto registerUser(@Validated(GroupValidation.Create.class) @RequestBody UserSaveDto userDto){
        return userService.save(userDto);
    }

    @PostMapping("/login")
    public UserTokenDto loginUser(@Validated(GroupValidation.Login.class) @RequestBody UserSaveDto userDto){
        return userService.login(userDto);
    }
}
