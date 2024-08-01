package com.thoughts.mapper.user;

import com.thoughts.dto.user.ProfileReadUserDto;
import com.thoughts.mapper.Mapper;
import com.thoughts.model.User;

public class ProfileReadUserMapper implements Mapper<User, ProfileReadUserDto> {

    @Override
    public ProfileReadUserDto map(User object) {
        return ProfileReadUserDto.builder()
                .id(object.getId())
                .username(object.getUsername())
                .subscribers(object.getSubscribers().size())
                .subscriptions(object.getSubscriptions().size())
                .build();
    }
}
