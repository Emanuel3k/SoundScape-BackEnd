package com.emanuel3k.soundscape_backend.domain.spotify.dto;


public record SpotifyAcessTokenResponseDTO(
        String access_token,
        String token_type,
        String scope,
        Long expires_in,
        String refresh_token
) {
}
