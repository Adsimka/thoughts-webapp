package com.thoughts.mapper;

import com.thoughts.dto.CreateUserDto;
import com.thoughts.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements Mapper<CreateUserDto, User> {

    @Override
    public User map(CreateUserDto object) {
        return User.builder()
                .username(object.getEmail())
                .password(object.getPassword())
                .build();
    }
}
