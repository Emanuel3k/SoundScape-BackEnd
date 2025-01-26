package com.emanuel3k.soundscape_backend.service.v1.user;

import com.emanuel3k.soundscape_backend.domain.auth.dto.LoginRequestDTO;
import com.emanuel3k.soundscape_backend.domain.user.dto.CreateUserDTO;
import com.emanuel3k.soundscape_backend.domain.user.model.User;
import com.emanuel3k.soundscape_backend.infra.exception.BadRequestException;
import com.emanuel3k.soundscape_backend.infra.security.TokenService;
import com.emanuel3k.soundscape_backend.repository.v1.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private TokenService tokenService;

  @InjectMocks
  private UserService userService;

  @Test
  @DisplayName("Should create a new user successfully")
  void createUser1() {
    User user = new User("username", "email", "password");

    when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
    when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
    when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
    when(userRepository.save(any(User.class))).thenReturn(user);

    CreateUserDTO createUserDTO = new CreateUserDTO(user.getUsername(), user.getEmail(), user.getPassword());
    userService.createUser(createUserDTO);

    verify(userRepository, times(1)).findByUsername(any());
    verify(userRepository, times(1)).findByEmail(any());
    verify(passwordEncoder, times(1)).encode(any());
    verify(userRepository, times(1)).save(any());
  }

  @Test
  @DisplayName("Shouldn't create a new user with an existing username")
  void createUser2() {
    User user = new User("username", "email", "password");

    when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

    CreateUserDTO createUserDTO = new CreateUserDTO(user.getUsername(), user.getEmail(), user.getPassword());
    BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> userService.createUser(createUserDTO));

    Assertions.assertEquals("Username already exists", exception.getMessage());
    verify(userRepository, times(1)).findByUsername(any());
    verify(userRepository, times(0)).findByEmail(any());
    verify(passwordEncoder, times(0)).encode(any());
    verify(userRepository, times(0)).save(any());
  }

  @Test
  @DisplayName("Shouldn't create a new user with an existing email")
  void createUser3() {
    User user = new User("username", "email", "password");
    when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
    when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

    CreateUserDTO createUserDTO = new CreateUserDTO(user.getUsername(), user.getEmail(), user.getPassword());
    BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> userService.createUser(createUserDTO));

    Assertions.assertEquals("Email already exists", exception.getMessage());
    verify(userRepository, times(1)).findByUsername(any());
    verify(userRepository, times(1)).findByEmail(any());
    verify(passwordEncoder, times(0)).encode(any());
    verify(userRepository, times(0)).save(any());
  }

  @Test
  @DisplayName("Should login a user successfully")
  void loginUser1() {
    User user = new User("username", "email", "password");

    when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(user.getPassword(), user.getPassword())).thenReturn(true);
    when(tokenService.generateToken(user)).thenReturn("token");

    LoginRequestDTO loginRequestDTO = new LoginRequestDTO(user.getUsername(), user.getPassword());
    userService.loginUser(loginRequestDTO);

    verify(userRepository, times(1)).findByUsername(any());
    verify(passwordEncoder, times(1)).matches(any(), any());
    verify(tokenService, times(1)).generateToken(any());
  }

  @Test
  @DisplayName("Shouldn't login a user with an incorrect username")
  void loginUser2() {
    User user = new User("username", "email", "password");

    when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

    LoginRequestDTO loginRequestDTO = new LoginRequestDTO(user.getUsername(), user.getPassword());
    BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> userService.loginUser(loginRequestDTO));

    Assertions.assertEquals("Username or password is incorrect", exception.getMessage());
    verify(userRepository, times(1)).findByUsername(any());
    verify(passwordEncoder, times(0)).matches(any(), any());
    verify(tokenService, times(0)).generateToken(any());
  }
  
  @Test
  @DisplayName("Shouldn't login a user with an incorrect password")
  void loginUser3() {
    User user = new User("username", "email", "password");

    when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(user.getPassword(), user.getPassword())).thenReturn(false);

    LoginRequestDTO loginRequestDTO = new LoginRequestDTO(user.getUsername(), user.getPassword());
    BadRequestException exception = Assertions.assertThrows(BadRequestException.class, () -> userService.loginUser(loginRequestDTO));

    Assertions.assertEquals("Username or password is incorrect", exception.getMessage());
    verify(userRepository, times(1)).findByUsername(any());
    verify(passwordEncoder, times(1)).matches(any(), any());
    verify(tokenService, times(0)).generateToken(any());
  }
}