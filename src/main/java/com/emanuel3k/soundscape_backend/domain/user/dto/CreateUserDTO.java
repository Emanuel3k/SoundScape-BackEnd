package com.emanuel3k.soundscape_backend.domain.user.dto;

import com.emanuel3k.soundscape_backend.domain.user.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserDTO(
        @NotBlank(message = "Username is required")
        @Size(min = 2, max = 20, message = "Username must be between 2 and 20 characters")
        String username,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        String password
) {

  public User toUser() {
    return new User(username, email, password);
  }
}
