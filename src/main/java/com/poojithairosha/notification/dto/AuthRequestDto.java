package com.poojithairosha.notification.dto;

import lombok.Builder;

@Builder
public record AuthRequestDto(
        String email,
        String password
) {
}
