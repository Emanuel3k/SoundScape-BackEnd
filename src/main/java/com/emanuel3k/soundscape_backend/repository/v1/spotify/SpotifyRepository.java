package com.emanuel3k.soundscape_backend.repository.v1.spotify;


import com.emanuel3k.soundscape_backend.domain.spotify.domain.Spotify;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpotifyRepository extends JpaRepository<Spotify, Long> {
  
}
