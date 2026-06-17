package com.bookmie.lit.auths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookmie.lit.auths.dtos.GetTokenDto;
import com.bookmie.lit.auths.dtos.RegisterDto;
import com.bookmie.lit.auths.dtos.VerifyUserDto;
import com.bookmie.lit.utils.dtos.ApiResponse;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/auths")
public class AuthsController {

  @Autowired
  private AuthsService authsService;

  @PostMapping("/register")
  public ResponseEntity<ApiResponse<Void>> registerUser(@RequestBody RegisterDto requestData) {
    this.authsService.registerUser(requestData);
    ApiResponse<Void> response = ApiResponse.<Void>builder()
        .success(true)
        .message("Verification code sent to " + requestData.email())
        .timestamp(LocalDateTime.now())
        .build();
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PostMapping("/verify-user")
  public ResponseEntity<ApiResponse<Void>> verifyUser(@RequestBody VerifyUserDto requestData) {
    this.authsService.verifyUser(requestData);
    ApiResponse<Void> response = ApiResponse.<Void>builder()
        .success(true)
        .message("Account has been created successfully")
        .timestamp(LocalDateTime.now())
        .build();
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PostMapping("/obtain-token")
  public ResponseEntity<ApiResponse<Map<String, String>>> obtainToken(@RequestBody GetTokenDto requestData) {
    Map<String, String> tokenData = this.authsService.getToken(requestData.email(), requestData.password());
    ApiResponse<Map<String, String>> response = ApiResponse.<Map<String, String>>builder()
        .success(true)
        .message("login successful")
        .data(tokenData)
        .timestamp(LocalDateTime.now())
        .build();
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
