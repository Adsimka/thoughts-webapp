package com.thoughts.mapper;

import com.thoughts.dto.user.ReadUserDto;
import com.thoughts.model.User;

public class ReadUserMapper implements Mapper<User, ReadUserDto> {

    @Override
    public ReadUserDto map(User object) {
        return ReadUserDto.builder()
                .username(object.getUsername())
                .password(object.getPassword())
                .build();
    }
}
