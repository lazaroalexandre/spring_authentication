package com.example.spring_authentication.domain.validation;

import jakarta.validation.groups.Default;

public interface GroupValidation {
    interface Create extends Default{}
    interface Login extends Default{} 
}
