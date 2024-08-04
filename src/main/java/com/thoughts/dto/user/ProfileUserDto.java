package com.thoughts.dto.user;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ProfileUserDto {

    Long id;

    String username;

    Integer subscribers;

    Integer subscriptions;

    Boolean isSubscribed;
}
