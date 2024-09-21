package com.poojithairosha.notification.service;

import com.poojithairosha.notification.dto.UserRequestDto;
import com.poojithairosha.notification.dto.UserResponseDto;
import com.poojithairosha.notification.entity.User;
import com.poojithairosha.notification.mapper.UserMapper;
import com.poojithairosha.notification.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User saveUser(UserRequestDto requestDto) {
        log.info("Saving user: {}", requestDto);
        var user = UserMapper.mapToEntity(requestDto);

        String token = UUID.randomUUID().toString();
        user.setToken(token);

        var saved = userRepository.save(user);
        log.info("Saved user: {}", saved.getId());
        return saved;
    }

    public List<UserResponseDto> findAll() {
        log.info("Finding all users");
        var list = userRepository.findAll().stream().map(UserMapper::mapToDto).toList();
        log.info("Found {} users", list.size());
        return list;
    }

    public UserResponseDto findById(Long id) {
        log.info("Finding user by id: {}", id);
        var user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        log.info("Found user: {}", user.getId());
        return UserMapper.mapToDto(user);
    }

    public void deleteUser(Long id) {
        log.info("Deleting user by id: {}", id);
        userRepository.deleteById(id);
        log.info("Deleted user: {}", id);
    }

    public UserResponseDto updateUser(Long id, UserRequestDto requestDto) {
        log.info("Updating user by id: {}", id);
        var user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setName(requestDto.name());
        user.setEmail(requestDto.email());
        user.setPassword(requestDto.password());
        var updated = userRepository.save(user);
        log.info("Updated user: {}", updated.getId());
        return UserMapper.mapToDto(updated);
    }

}
