package com.poojithairosha.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    private String notificationName;
    private String subject;
    private String templateUrl;
    private List<String> to;
    private List<String> cc;
    private List<String> mobileNo;
    private Map<String, Object> additionalParams;
}
