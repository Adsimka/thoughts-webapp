package com.thoughts.mapper;

import com.thoughts.dto.UserCreateDto;
import com.thoughts.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements Mapper<UserCreateDto, User> {

    @Override
    public User map(UserCreateDto object) {
        return User.builder()
                .username(object.getEmail())
                .password(object.getPassword())
                .matchingPassword(object.getMatchingPassword())
                .build();
    }
}
