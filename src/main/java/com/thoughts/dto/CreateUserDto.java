package com.thoughts.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUserDto {

    @NotNull
    @NotEmpty
    String username;

    @NotNull
    @NotEmpty
    @Email
    String email;

    @NotNull
    @NotEmpty
    String password;
}
