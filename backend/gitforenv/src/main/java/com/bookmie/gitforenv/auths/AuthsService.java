package com.bookmie.gitforenv.auths;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;
import com.bookmie.gitforenv.auths.dtos.PendingUserDto;
import com.bookmie.gitforenv.auths.dtos.RegisterDto;
import com.bookmie.gitforenv.auths.dtos.VerifyUserDto;
import com.bookmie.gitforenv.auths.models.UserModel;
import com.bookmie.gitforenv.auths.repos.UserRepository;
import com.bookmie.gitforenv.configs.services.EmailService;
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

  @Autowired
  private EmailService emailService;

  public ResponseDto registerUser(RegisterDto data) {
    String email = data.email();
    String password = passwordEncoder.encode(data.password());
    String pendingUserId = "pending_user_" + email;
    String otpCOde = Contrib.generateOtpCode().toString();
    String hashedOtp = this.passwordEncoder.encode(otpCOde);
    PendingUserDto newPendingUser = new PendingUserDto(email, password, hashedOtp);
    if (this.userRepository.findByEmail(email).isPresent()) {
      return new ResponseDto(200, "Email already exists", null);
    }
    try {
      String strNewPendingUser = objectMapper.writeValueAsString(newPendingUser);
      if (this.redisTemplate.opsForValue().get(pendingUserId) != null) {
        this.redisTemplate.opsForValue().getAndDelete(pendingUserId);
      }
      this.redisTemplate.opsForValue().set(pendingUserId, strNewPendingUser, 15, TimeUnit.MINUTES);
      this.emailService.sendSimpleEmail(email, "Account Verification", otpCOde);
      return new ResponseDto(200, "Verification code sent to " + email, null);
    } catch (Exception e) {
      System.out.println(e);
      return new ResponseDto(400, "Registration failed", null);
    }
  }

  public ResponseDto verifyUser(VerifyUserDto data) {
    String pendingUserId = "pending_user_" + data.email();
    Object pendingUser = this.redisTemplate.opsForValue().getAndDelete(pendingUserId);
    if (pendingUser == null) {
      Map<String, String> emailPayload = new HashMap<>();
      emailPayload.put("email", data.email());
      return new ResponseDto(400, "Invalid code or code", emailPayload);
    }
    try {
      PendingUserDto userObj = this.objectMapper.readValue(pendingUser.toString(), PendingUserDto.class);
      if (this.passwordEncoder.matches(data.token(), userObj.getOtp())) {
        UserModel newUser = new UserModel(userObj.getEmail(), userObj.getPassword());
        this.userRepository.save(newUser);
        return new ResponseDto(200, "Account has been created successfully", null);
      }
    } catch (Exception e) {
      System.out.println(e);
      return new ResponseDto(500, "Invalid code or code", null);
    }
    return new ResponseDto(400, "Invalid code or code", null);
  }
}
