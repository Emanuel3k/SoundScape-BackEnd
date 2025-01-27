package com.emanuel3k.soundscape_backend.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.emanuel3k.soundscape_backend.domain.user.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

  @Value("${api.security.token.secret}")
  private String secret;

  public String generateToken(User user) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);

      return JWT.create()
              .withIssuer("SoundScape")
              .withSubject(user.getUsername())
              .withClaim("role", user.getRole().toString())
              .withExpiresAt(GenerateExpirationDate())
              .sign(algorithm);

    } catch (JWTCreationException e) {
      throw new RuntimeException("Error while generating token");
    }
  }

  public String validateToken(String token) {
    try {
      Algorithm algorithm = Algorithm.HMAC256(secret);

      return JWT.require(algorithm)
              .withIssuer("SoundScape")
              .build()
              .verify(token)
              .getSubject();
    } catch (JWTVerificationException jwtVerificationException) {
      return null;
    }
  }

  public String recoverToken(HttpServletRequest request) {
    var authHeader = request.getHeader("Authorization");
    if (authHeader == null) return null;
    return authHeader.replace("Bearer ", "");
  }

  private Instant GenerateExpirationDate() {
    return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
  }
}
