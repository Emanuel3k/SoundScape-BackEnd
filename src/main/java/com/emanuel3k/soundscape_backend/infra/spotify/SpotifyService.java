package com.emanuel3k.soundscape_backend.infra.spotify;

import com.emanuel3k.soundscape_backend.domain.spotify.domain.Spotify;
import com.emanuel3k.soundscape_backend.domain.spotify.dto.SpotifyAcessTokenResponseDTO;
import com.emanuel3k.soundscape_backend.domain.user.model.User;
import com.emanuel3k.soundscape_backend.infra.exception.BadRequestException;
import com.emanuel3k.soundscape_backend.infra.exception.InternalServerErrorException;
import com.emanuel3k.soundscape_backend.repository.v1.spotify.SpotifyRepository;
import com.emanuel3k.soundscape_backend.repository.v1.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Service
public class SpotifyService {

  @Value("${spotify.client.id}")
  private String clientId;

  @Value("${spotify.client.secret}")
  private String clientSecret;

  @Value("${server.baseUrl}")
  private String apiBaseUrl;

  @Value("${spotify.base.url}")
  private String spotifyBaseUrl;

  private final SpotifyRepository spotifyRepository;

  private final UserRepository userRepository;

  public SpotifyService(SpotifyRepository spotifyRepository, UserRepository userRepository) {
    this.spotifyRepository = spotifyRepository;
    this.userRepository = userRepository;
  }

  public void getAccessToken(String code, String username) {

    String redirectUri = apiBaseUrl + "/v1/spotify/callback";
    String authHeader = "Basic " + Base64.getEncoder()
            .encodeToString((clientId + ":" + clientSecret)
                    .getBytes());

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    headers.set("Authorization", authHeader);

    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    formData.add("code", code);
    formData.add("redirect_uri", redirectUri);
    formData.add("grant_type", "authorization_code");

    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);

    RestTemplate restTemplate = new RestTemplate();
    SpotifyAcessTokenResponseDTO responseDTO = restTemplate
            .postForEntity(spotifyBaseUrl + "/api/token", requestEntity, SpotifyAcessTokenResponseDTO.class).getBody();

    if (responseDTO == null) {
      throw new InternalServerErrorException("Error getting access token");
    }

    Spotify spotify = new Spotify();
    spotify.setAccessToken(responseDTO.access_token());
    spotify.setTokenType(responseDTO.token_type());
    spotify.setScope(responseDTO.scope());
    spotify.setRefreshToken(responseDTO.refresh_token());
    spotify.setExpiresIn(responseDTO.expires_in());

    User user = userRepository.findByUsername(username).orElseThrow(() -> new BadRequestException("User not found"));

    spotify.setUser(user);

    spotifyRepository.save(spotify);
  }
}
