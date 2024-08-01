package com.thoughts.mapper.user;

import com.thoughts.dto.user.ProfileUserDto;
import com.thoughts.mapper.Mapper;
import com.thoughts.model.User;

public class ProfileUserMapper implements Mapper<User, ProfileUserDto> {

    @Override
    public ProfileUserDto map(User object) {
        return ProfileUserDto.builder()
                .id(object.getId())
                .username(object.getUsername())
                .subscribers(object.getSubscribers().size())
                .subscriptions(object.getSubscriptions().size())
                .build();
    }
}
