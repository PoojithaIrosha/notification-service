package com.poojithairosha.notification.mapper;

import com.poojithairosha.notification.dto.UserRequestDto;
import com.poojithairosha.notification.dto.UserResponseDto;
import com.poojithairosha.notification.entity.User;

public class UserMapper {

    public static UserResponseDto mapToDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User mapToEntity(UserRequestDto requestDto) {
        return User.builder()
                .name(requestDto.name())
                .email(requestDto.email())
                .password(requestDto.password())
                .build();
    }

}
