package com.bookmie.lit.auths;

import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.Map;

@RestController
@RequestMapping("/auths")
public class AuthsController {

  @Autowired
  private AuthsService authsService;

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
    Map<String, String> tokenData = this.authsService.getToken(requestData.email(), requestData.password());
    return ResponseFactory.ok("login successful", tokenData);
  }
}
