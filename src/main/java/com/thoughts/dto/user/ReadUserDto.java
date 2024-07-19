package com.thoughts.dto.user;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ReadUserDto {

    Long id;

    String username;

    String password;
}
