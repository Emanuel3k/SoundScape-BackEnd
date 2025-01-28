package com.emanuel3k.soundscape_backend.controller.v1;

import com.emanuel3k.soundscape_backend.infra.cache.CacheService;
import com.emanuel3k.soundscape_backend.infra.exception.BadRequestException;
import com.emanuel3k.soundscape_backend.infra.security.TokenService;
import com.emanuel3k.soundscape_backend.infra.spotify.SpotifyService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/v1/spotify")
public class SpotifyController {

  @Value("${spotify.client.id}")
  private String clientId;

  @Value("${spotify.client.secret}")
  private String clientSecret;

  @Value("${server.baseUrl}")
  private String apiBaseUrl;

  @Value("${spotify.base.url}")
  private String spotifyBaseUrl;

  private final TokenService tokenService;

  private final SpotifyService spotifyService;

  private final CacheService cacheService;

  public SpotifyController(TokenService tokenService, SpotifyService spotifyService, CacheService cacheService) {
    this.tokenService = tokenService;
    this.spotifyService = spotifyService;
    this.cacheService = cacheService;
  }

  @GetMapping("/auth")
  public void auth(HttpServletRequest request, HttpServletResponse response) {
    try {
      String username = tokenService.recoverToken(request);
      String state = new Random(16).toString();

      String scope = "user-read-private user-read-email";

      URI uri = UriComponentsBuilder.
              fromUriString(spotifyBaseUrl).
              path("/authorize")
              .queryParam("response_type", "code")
              .queryParam("client_id", clientId).queryParam("scope", scope)
              .queryParam("redirect_uri", apiBaseUrl + "/v1/spotify/callback").queryParam("state", state)
              .build().toUri();

      cacheService.put(state, username, 5, TimeUnit.MINUTES);

      response.sendRedirect(uri.toString());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @GetMapping("/callback")
  public void callback(HttpServletRequest request) {
    try {
      String code = request.getParameter("code");
      String state = request.getParameter("state");

      String username = cacheService.get(state);

      if (username == null) {
        throw new BadRequestException("Invalid state");
      }

      spotifyService.getAccessToken(code, username);

      ResponseEntity.noContent().build();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
