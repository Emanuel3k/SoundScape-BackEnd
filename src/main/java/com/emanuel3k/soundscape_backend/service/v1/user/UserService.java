package com.emanuel3k.soundscape_backend.service.v1.user;

import com.emanuel3k.soundscape_backend.domain.auth.dto.LoginRequestDTO;
import com.emanuel3k.soundscape_backend.domain.auth.dto.TokenResponseDTO;
import com.emanuel3k.soundscape_backend.domain.user.dto.CreateUserDTO;
import com.emanuel3k.soundscape_backend.domain.user.model.User;
import com.emanuel3k.soundscape_backend.infra.exception.BadRequestException;
import com.emanuel3k.soundscape_backend.infra.security.TokenService;
import com.emanuel3k.soundscape_backend.repository.v1.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  private final TokenService tokenService;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.tokenService = tokenService;
  }

  @Override
  public User createUser(CreateUserDTO body) {

    Optional<User> existingUser = userRepository.findByUsername(body.username());

    if (existingUser.isPresent()) {
      throw new BadRequestException("Username already exists");
    }

    existingUser = userRepository.findByEmail(body.email());

    if (existingUser.isPresent()) {
      throw new BadRequestException("Email already exists");
    }

    User user = body.toUser();

    user.setPassword(passwordEncoder.encode(user.getPassword()));

    return userRepository.save(user);
  }

  @Override
  public TokenResponseDTO loginUser(LoginRequestDTO body) {
    User user = userRepository.findByUsername(body.username()).orElseThrow(() -> new BadRequestException("Username or password is incorrect"));

    if (!passwordEncoder.matches(body.password(), user.getPassword())) {
      throw new BadRequestException("Username or password is incorrect");
    }

    String token = tokenService.generateToken(user);

    return new TokenResponseDTO(token);
  }
}
