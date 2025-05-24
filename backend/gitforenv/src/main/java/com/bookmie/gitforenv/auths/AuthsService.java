package com.bookmie.gitforenv.auths;

import java.util.concurrent.TimeUnit;

import org.apache.tomcat.util.security.PrivilegedGetTccl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;
import com.bookmie.gitforenv.auths.dtos.PendingUserDto;
import com.bookmie.gitforenv.auths.dtos.RegisterDto;
import com.bookmie.gitforenv.auths.repos.UserRepository;
import com.bookmie.gitforenv.utils.dtos.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bookmie.gitforenv.utils.Contrib;

@Service
public class AuthsService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private RedisTemplate redisTemplate;

  public ResponseDto registerUser(RegisterDto data) {
    String email = data.email();
    String password = passwordEncoder.encode(data.password());
    String pendingUserId = "pending_user_" + this.passwordEncoder.encode(email);
    String otpCOde = Contrib.generateOtpCode().toString();
    String hashedOtp = this.passwordEncoder.encode(otpCOde);
    PendingUserDto newPendingUser = new PendingUserDto(email, password, hashedOtp);
    try {
      String strNewPendingUser = objectMapper.writeValueAsString(newPendingUser);
      System.out.println(strNewPendingUser);
      this.redisTemplate.opsForValue().set(pendingUserId, strNewPendingUser, 15, TimeUnit.MINUTES);
      return new ResponseDto(200, "Registration successfull, please verify your email", strNewPendingUser);
    } catch (Exception e) {
      System.out.println(e);
      return new ResponseDto(400, "Registration failed", null);
    }
  }
}
