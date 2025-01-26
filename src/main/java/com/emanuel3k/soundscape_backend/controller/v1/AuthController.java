package com.emanuel3k.soundscape_backend.controller.v1;

import com.emanuel3k.soundscape_backend.domain.auth.dto.LoginRequestDTO;
import com.emanuel3k.soundscape_backend.domain.user.dto.CreateUserDTO;
import com.emanuel3k.soundscape_backend.domain.user.model.User;
import com.emanuel3k.soundscape_backend.service.v1.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

  @Autowired
  UserService userService;

  @PostMapping("/register")
  public ResponseEntity<User> register(@Valid @RequestBody CreateUserDTO body) {
    try {
      User response = userService.createUser(body);

      URI location = new URI("/v1/users/" + response.getUserId());

      return ResponseEntity.created(location).body(response);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @PostMapping(value = "/login")
  public ResponseEntity<String> userLogin(@Valid @RequestBody LoginRequestDTO body) {
    try {
      String response = userService.loginUser(body);

      return ResponseEntity.ok().body(response);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
