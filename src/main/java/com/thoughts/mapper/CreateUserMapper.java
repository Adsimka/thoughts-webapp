package com.thoughts.mapper;

import com.thoughts.dto.user.CreateUserDto;
import com.thoughts.model.User;
import org.springframework.stereotype.Component;

@Component
public class CreateUserMapper implements Mapper<CreateUserDto, User> {

    @Override
    public User map(CreateUserDto object) {
        return User.builder()
                .username(object.getUsername())
                .email(object.getEmail())
                .password(object.getPassword())
                .build();
    }

    public User map(CreateUserDto object, User user) {
        user.setUsername(object.getUsername());
        user.setEmail(user.getEmail());
        user.setPassword(user.getPassword());

        return user;
    }
}
