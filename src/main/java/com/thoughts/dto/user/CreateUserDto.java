package com.thoughts.dto.user;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDto {

    @NotBlank(message = "Username cannot be empty")
    String username;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email is not correct")
    String email;

    @NotBlank(message = "Password cannot be empty")
    String password;

    @Transient
    @NotBlank(message = "Password confirmation cannot be empty")
    String matchingPassword;
}
