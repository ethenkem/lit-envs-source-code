package com.bookmie.lit.users;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bookmie.lit.users.dtos.SearchRequestDto;
import com.bookmie.lit.utils.exceptions.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
class UserService {

  private final UserRepository userRepository;

  public Map<String, String> searchUser(SearchRequestDto data) {
    Optional<UserModel> user = this.userRepository.findByEmail(data.userEmail());
    if (user.isEmpty()) {
      throw new ResourceNotFoundException("User", data.userEmail());
    }
    Map<String, String> userData = new HashMap<>();
    userData.put("email", user.get().getEmail());
    userData.put("id", user.get().getId());
    return userData;
  }

  public void appendUser() {
  }
}
