package com.emanuel3k.soundscape_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SoundscapeBackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(SoundscapeBackendApplication.class, args);
  }

}
