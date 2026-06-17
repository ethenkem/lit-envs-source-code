package com.bookmie.lit.auths;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookmie.lit.auths.dtos.GetTokenDto;
import com.bookmie.lit.auths.dtos.RegisterDto;
import com.bookmie.lit.auths.dtos.VerifyUserDto;
import com.bookmie.lit.utils.ResponseFactory;
import com.bookmie.lit.utils.dtos.ApiResponse;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RestController
@RequestMapping("/auths")
@RequiredArgsConstructor
public class AuthsController {

  private final AuthsService authsService;

  @PostMapping("/register")
  public ResponseEntity<ApiResponse<Void>> registerUser(@RequestBody RegisterDto requestData) {
    this.authsService.registerUser(requestData);
    return ResponseFactory.ok("Verification code sent to " + requestData.email());
  }

  @PostMapping("/verify-user")
  public ResponseEntity<ApiResponse<Void>> verifyUser(@RequestBody VerifyUserDto requestData) {
    this.authsService.verifyUser(requestData);
    return ResponseFactory.ok("Account has been created successfully");
  }

  @PostMapping("/obtain-token")
  public ResponseEntity<ApiResponse<Map<String, String>>> obtainToken(@RequestBody GetTokenDto requestData) {
    return ResponseFactory.ok("login successful", this.authsService.getToken(requestData.email(), requestData.password()));
  }
}
