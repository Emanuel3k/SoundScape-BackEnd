package com.emanuel3k.soundscape_backend.service.v1.user;

import com.emanuel3k.soundscape_backend.domain.user.dto.CreateUserDTO;
import com.emanuel3k.soundscape_backend.domain.user.model.User;
import com.emanuel3k.soundscape_backend.repository.v1.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {
  @Autowired
  UserRepository userRepository;

  @Override
  public User createUser(CreateUserDTO body) {

    return userRepository.save(body.toUser());
  }
}
