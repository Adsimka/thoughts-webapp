package com.thoughts.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserDto {

    @NotNull
    @NotEmpty
    String username;

    @NotNull
    @NotEmpty
    String password;

    @NotNull
    @NotEmpty
    boolean active;

    @NotNull
    @NotEmpty
    String email;
}
