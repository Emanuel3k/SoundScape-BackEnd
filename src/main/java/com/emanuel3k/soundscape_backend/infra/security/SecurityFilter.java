package com.emanuel3k.soundscape_backend.infra.security;

import com.emanuel3k.soundscape_backend.domain.user.model.User;
import com.emanuel3k.soundscape_backend.repository.v1.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class SecurityFilter extends OncePerRequestFilter {

  private final TokenService tokenService;
  private final UserRepository userRepository;

  public SecurityFilter(TokenService tokenService, UserRepository userRepository) {
    this.tokenService = tokenService;
    this.userRepository = userRepository;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    logger.info("Request: " + request.getRequestURI());

    String token = tokenService.recoverToken(request);
    String subject = tokenService.validateToken(token);

    if (subject != null) {
      User user = userRepository.findByUsername(subject).orElseThrow(() -> new RuntimeException("User not found"));
      List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole().toString()));
      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    filterChain.doFilter(request, response);
  }
}
