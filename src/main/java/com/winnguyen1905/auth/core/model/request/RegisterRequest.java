package com.winnguyen1905.auth.core.model.request;

import com.winnguyen1905.auth.common.constant.AccountType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    @NotBlank(message = "username not be blank") @Pattern(regexp = "^[a-zA-Z0-9]{8,20}$", message = "username must be of 8 to 20 length with no special characters") String username,

    @NotBlank @Size(min = 8, message = "The password must be length >= 8") String password,

    @NotBlank @Email(message = "Email format invalid") String email,

    String phone,

    @NotBlank String fullName,

    @NotNull AccountType accountType,

    String address
) implements AbstractModel {}
