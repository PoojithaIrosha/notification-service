package com.poojithairosha.notification.rest;

import com.poojithairosha.notification.dto.UserRequestDto;
import com.poojithairosha.notification.dto.UserResponseDto;
import com.poojithairosha.notification.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findAll() {
        var users = userService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserResponseDto> findById(Long id) {
        var user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("{id}")
    public ResponseEntity<UserResponseDto> update(@PathVariable Long id, @RequestBody UserRequestDto requestDto) {
        var user = userService.updateUser(id, requestDto);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
