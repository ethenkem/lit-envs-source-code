package com.bookmie.lit.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookmie.lit.users.dtos.SearchRequestDto;
import com.bookmie.lit.utils.dtos.ApiResponse;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private UserService userService;

  @PostMapping("/search")
  public ResponseEntity<ApiResponse<Map<String, String>>> searchUser(@RequestBody SearchRequestDto data) {
    Map<String, String> userData = this.userService.searchUser(data);
    ApiResponse<Map<String, String>> response = ApiResponse.<Map<String, String>>builder()
        .success(true)
        .message("user found")
        .data(userData)
        .timestamp(LocalDateTime.now())
        .build();
    return ResponseEntity.ok(response);
  }
}
