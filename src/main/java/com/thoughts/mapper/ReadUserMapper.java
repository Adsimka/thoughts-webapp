package com.thoughts.mapper;

import com.thoughts.dto.user.ReadUserDto;
import com.thoughts.model.User;
import org.springframework.stereotype.Component;

@Component
public class ReadUserMapper implements Mapper<User, ReadUserDto> {

    @Override
    public ReadUserDto map(User object) {
        return ReadUserDto.builder()
                .id(object.getId())
                .username(object.getUsername())
                .password(object.getPassword())
                .build();
    }
}
