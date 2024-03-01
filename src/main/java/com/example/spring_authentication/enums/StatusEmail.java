package com.example.spring_authentication.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum StatusEmail {
    SEND("send"),
    ERROR("error");
    private String sendStatus;
}
