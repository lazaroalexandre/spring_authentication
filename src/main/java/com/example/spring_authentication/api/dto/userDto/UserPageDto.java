package com.example.spring_authentication.api.dto.userDto;

import java.util.List;

public record UserPageDto(
        List<UserDetailDto> userDto, long totalElements, int totalPages) {
}
