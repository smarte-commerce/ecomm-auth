package com.winnguyen1905.gateway.core.model.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Setter
@Getter
@Builder
public class LoginRequest {
    @Pattern(
        regexp = "^[a-zA-Z0-9]{8,20}$",
        message = "username must be of 8 to 20 length with no special characters"
    )
    private String username;

    @NotBlank(message = "password invalid")
    @Size(min = 8, max = 20, message = "password's length must >= 8")
    private String password;
}