package com.emanuel3k.soundscape_backend.repository.v1.user;

import com.emanuel3k.soundscape_backend.domain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
