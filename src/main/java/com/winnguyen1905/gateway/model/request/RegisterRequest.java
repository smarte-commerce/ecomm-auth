package com.winnguyen1905.gateway.model.request;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
public class RegisterRequest {
    @NotBlank(message = "username not be blank")
    @Pattern(regexp = "^[a-zA-Z0-9]{8,20}$", message = "username must be of 8 to 20 length with no special characters")
    private String username;

    @NotBlank
    @Size(min = 8, message = "The password must be length >= 8")
    private String password;

    @NotBlank
    @Email(message = "Email format invalid")
    private String email;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private List<UUID> roles;
}