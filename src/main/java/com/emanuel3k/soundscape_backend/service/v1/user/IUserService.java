package com.emanuel3k.soundscape_backend.service.v1.user;

import com.emanuel3k.soundscape_backend.domain.auth.dto.LoginRequestDTO;
import com.emanuel3k.soundscape_backend.domain.user.dto.CreateUserDTO;
import com.emanuel3k.soundscape_backend.domain.user.model.User;

public interface IUserService {
  User createUser(CreateUserDTO body);

  String loginUser(LoginRequestDTO body);
}
