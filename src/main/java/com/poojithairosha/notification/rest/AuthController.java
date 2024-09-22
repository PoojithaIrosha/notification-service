package com.poojithairosha.notification.rest;

import com.poojithairosha.notification.dto.AuthRequestDto;
import com.poojithairosha.notification.dto.EmailVerifyRespDto;
import com.poojithairosha.notification.dto.UserRequestDto;
import com.poojithairosha.notification.dto.UserResponseDto;
import com.poojithairosha.notification.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("register")
    public ResponseEntity<UserResponseDto> save(@RequestBody UserRequestDto requestDto) {
        var user = authService.register(requestDto);
        return ResponseEntity.ok(user);
    }

    @PostMapping("login")
    public ResponseEntity<UserResponseDto> authenticate(@RequestBody AuthRequestDto requestDto) {
        var resp = authService.authenticate(requestDto);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("verify")
    public ResponseEntity<EmailVerifyRespDto> verifyEmail(@RequestParam String token) {
        var resp = this.authService.verifyEmail(token);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        this.authService.forgotPassword(email);
        return ResponseEntity.ok("Password reset link sent to your email");
    }

}
