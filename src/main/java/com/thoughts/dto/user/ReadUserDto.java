package com.thoughts.dto.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ReadUserDto {

    @NotNull
    @NotEmpty
    String username;

    @NotNull
    @NotEmpty
    String password;
}
