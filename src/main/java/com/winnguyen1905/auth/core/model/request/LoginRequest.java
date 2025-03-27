package com.winnguyen1905.auth.core.model.request;

import jakarta.validation.constraints.*;
import lombok.*;

public record LoginRequest(
  @Pattern(regexp = "^[a-zA-Z0-9]{8,20}$", message = "username must be of 8 to 20 length with no special characters")
  String username,

  @NotBlank(message = "password invalid")
  @Size(min = 8, max = 20, message = "password's length must >= 8")
  String password
) implements AbstractModel {}
