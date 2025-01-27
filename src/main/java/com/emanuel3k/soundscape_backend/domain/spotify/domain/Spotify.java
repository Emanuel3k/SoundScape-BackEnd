package com.emanuel3k.soundscape_backend.domain.spotify.domain;

import com.emanuel3k.soundscape_backend.domain.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "spotify")
public class Spotify {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(nullable = false)
  String accessToken;

  @Column(nullable = false)
  String refreshToken;

  @Column(nullable = false)
  String tokenType;

  @Column(nullable = false)
  Long expiresIn;

  @Column(nullable = false)
  String scope;
  
  @OneToOne
  @JoinColumn(name = "userId", referencedColumnName = "userId")
  private User user;

  @Column(nullable = false)
  private LocalDateTime createdAt = LocalDateTime.now();

  @Column(nullable = false)
  private LocalDateTime updatedAt = LocalDateTime.now();
}
