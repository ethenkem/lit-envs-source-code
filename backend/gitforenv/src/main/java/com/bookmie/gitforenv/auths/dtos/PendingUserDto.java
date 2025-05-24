package com.bookmie.gitforenv.auths.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class PendingUserDto {
  private String email;
  private String password;
  private String otp;

  public PendingUserDto(String email, String password, String otp) {
    this.email = email;
    this.password = password;
    this.otp = otp;
  }
}
