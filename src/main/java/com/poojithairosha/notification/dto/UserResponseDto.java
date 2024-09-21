package com.poojithairosha.notification.dto;

import lombok.Builder;

@Builder
public record UserResponseDto(
        Long id,
        String name,
        String email
) {
}
