package com.bookmie.lit.users;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.bookmie.lit.users.dtos.SearchRequestDto;
import com.bookmie.lit.utils.dtos.ResponseDto;
import com.bookmie.lit.utils.exceptions.ApiException;

@Service
class UserService {

  @Autowired
  private UserRepository userRepository;

  public ResponseDto searchUser(SearchRequestDto data) {
    Optional<UserModel> user = this.userRepository.findByEmail(data.userEmail());
    if (user.isEmpty()) {
      throw new ApiException(HttpStatus.NOT_FOUND, "user not found");
    }
    Map<String, String> userData = new HashMap<>();
    userData.put("email", user.get().getEmail());
    userData.put("id", user.get().getId());
    return new ResponseDto(200, "user found", userData);
  }

  public void appendUser() {
  }
}
