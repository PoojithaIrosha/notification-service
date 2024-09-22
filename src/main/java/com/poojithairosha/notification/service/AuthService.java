package com.poojithairosha.notification.service;

import com.poojithairosha.notification.dto.*;
import com.poojithairosha.notification.mapper.UserMapper;
import com.poojithairosha.notification.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final NotificationService notificationService;

    public UserResponseDto authenticate(AuthRequestDto authRequestDto) {
        var user = userRepository.findByEmailAndPassword(authRequestDto.email(), authRequestDto.password()).orElseThrow(() -> new RuntimeException("User not found"));
        return UserMapper.mapToDto(user);
    }

    public UserResponseDto register(UserRequestDto userRequestDto) {
        var user = userService.saveUser(userRequestDto);
        String verifyUrl = "http://localhost:8090/api/v1/auth/verify?token=" + user.getToken();
        var notification = NotificationDto.builder()
                .to(List.of(user.getEmail()))
                .additionalParams(
                        Map.of("userName", user.getName(), "verificationUrl", verifyUrl)
                )
                .build();

        notificationService.sendNotification(notification, "email-verification");
        log.info("Email verification notification is sent");
        return UserMapper.mapToDto(user);
    }

    public EmailVerifyRespDto verifyEmail(String token) {
        var user = userRepository.findByToken(token).orElseThrow(() -> new RuntimeException("User not found"));
        user.setVerified(true);
        user.setToken(null);
        userRepository.save(user);
        return EmailVerifyRespDto.builder()
                .email(user.getEmail())
                .verified(true)
                .build();
    }

    public void forgotPassword(String email) {
        var user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        String verificationCode = String.valueOf((int) ((Math.random() * (999999 - 100000)) + 100000));
        user.setToken(verificationCode);
        userRepository.save(user);

        log.info("User verification code is updated");

        var notification = NotificationDto.builder()
                .to(List.of(user.getEmail()))
                .mobileNo(List.of("+94762873649"))
                .additionalParams(
                        Map.of("userName", user.getName(), "verificationCode", verificationCode)
                )
                .build();

        notificationService.sendNotification(notification, "reset-password");
        log.info("Forgot password notification is sent");
    }
}
