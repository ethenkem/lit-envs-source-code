package com.bookmie.lit.users;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookmie.lit.users.dtos.SearchRequestDto;
import com.bookmie.lit.utils.ResponseFactory;
import com.bookmie.lit.utils.dtos.ApiResponse;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/search")
  public ResponseEntity<ApiResponse<Map<String, String>>> searchUser(@RequestBody SearchRequestDto data) {
    return ResponseFactory.ok("user found", this.userService.searchUser(data));
  }
}
