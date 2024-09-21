package com.poojithairosha.notification.dto;

import lombok.Builder;

@Builder
public record EmailVerifyRespDto(
        String email,
        boolean verified
) {
}
