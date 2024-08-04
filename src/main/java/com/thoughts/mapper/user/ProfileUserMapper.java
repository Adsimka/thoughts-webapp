package com.thoughts.mapper.user;

import com.thoughts.dto.user.ProfileUserDto;
import com.thoughts.mapper.Mapper;
import com.thoughts.model.User;
import org.springframework.stereotype.Component;

@Component
public class ProfileUserMapper implements Mapper<User, ProfileUserDto> {

    public ProfileUserDto map(User object, boolean isSubscribed) {
        return ProfileUserDto.builder()
                .id(object.getId())
                .username(object.getUsername())
                .subscribers(object.getSubscribers().size())
                .subscriptions(object.getSubscriptions().size())
                .build();
    }

    @Override
    public ProfileUserDto map(User user) {
        return map(user, false);
    }
}
