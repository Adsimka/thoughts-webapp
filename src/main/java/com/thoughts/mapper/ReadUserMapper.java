package com.thoughts.mapper;

import com.thoughts.dto.user.ReadUserDto;
import com.thoughts.model.User;

public class ReadUserMapper implements Mapper<ReadUserDto, User> {

    @Override
    public User map(ReadUserDto object) {
        return User.builder()
                .username(object.getUsername())
                .password(object.getPassword())
                .build();
    }
}
