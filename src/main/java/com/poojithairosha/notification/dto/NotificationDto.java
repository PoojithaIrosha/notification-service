package com.poojithairosha.notification.dto;

import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record NotificationDto(
        List<String> to,
        List<String> cc,
        List<String> mobileNo,
        Map<String, Object> additionalParams
) {
}
