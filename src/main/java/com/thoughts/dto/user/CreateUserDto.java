package com.thoughts.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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