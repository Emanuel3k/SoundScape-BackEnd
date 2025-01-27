package com.emanuel3k.soundscape_backend.infra.cache;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class CacheService {

  private final Map<String, String> cache = new ConcurrentHashMap<>();
  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

  public void put(String key, String value, long duration, TimeUnit unit) {
    cache.put(key, value);
    scheduler.schedule(() -> cache.remove(key), duration, unit);
  }

  public String get(String key) {
    return cache.remove(key);
  }
}
