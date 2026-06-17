package com.bookmie.lit.auths;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bookmie.lit.auths.dtos.RegisterDto;
import com.bookmie.lit.auths.dtos.VerifyUserDto;
import com.bookmie.lit.configs.security.JwtService;
import com.bookmie.lit.configs.services.EmailService;
import com.bookmie.lit.users.UserModel;
import com.bookmie.lit.users.UserRepository;
import com.bookmie.lit.utils.Contrib;
import com.bookmie.lit.utils.EmailTemplateLoader;
import com.bookmie.lit.utils.exceptions.BadRequestException;
import com.bookmie.lit.utils.exceptions.DuplicateResourceException;
import com.bookmie.lit.utils.exceptions.ResourceNotFoundException;
import com.bookmie.lit.utils.exceptions.UnauthorizedException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthsService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final EmailService emailService;
  private final JwtService jwtService;

  public void registerUser(RegisterDto data) {
    String email = data.email();
    String password = passwordEncoder.encode(data.password());
    String otpCOde = Contrib.generateOtpCode().toString();
    String hashedOtp = this.passwordEncoder.encode(otpCOde);
    if (this.userRepository.findByEmail(email).isPresent()) {
      throw new DuplicateResourceException("Email already exists");
    }
    try {
      UserModel newUser = new UserModel(data.email(), password, hashedOtp);
      this.userRepository.save(newUser);
      String html = EmailTemplateLoader.loadTemplate("verification_email.html");
      String msg = html.replace("123456", otpCOde);
      this.emailService.sendHtmlEmail(email, "Lit Envs Verification", msg);
    } catch (Exception e) {
      System.out.println(e);
      throw new BadRequestException("Registration failed: " + e.getMessage());
    }
  }

  public void verifyUser(VerifyUserDto data) {
    System.out.println(data.token());
    Optional<UserModel> pendingUserOtp = this.userRepository.findByEmail(data.email());
    if (pendingUserOtp.isEmpty()) {
      throw new BadRequestException("Invalid code or email");
    }
    try {
      UserModel pendingUser = pendingUserOtp.get();
      if (this.passwordEncoder.matches(data.token(), pendingUser.getOtp())) {
        pendingUser.setOtp(null);
        this.userRepository.save(pendingUser);
        return;
      }
    } catch (Exception e) {
      System.out.println(e);
      throw new BadRequestException("Invalid code or OTP logic error: " + e.getMessage());
    }
    throw new BadRequestException("Invalid code or OTP");
  }

  public Map<String, String> getToken(String email, String password) {
    UserModel user = this.userRepository.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("User", email));
    if (this.passwordEncoder.matches(password, user.getPassword())) {
      String token = this.jwtService.generateToken(user.getId(), user.getEmail());
      Map<String, String> userObj = new HashMap<>();
      userObj.put("token", token);
      userObj.put("email", user.getEmail());
      userObj.put("lastLogedIn", user.getLastLogedIn() != null ? user.getLastLogedIn().toString() : null);
      userObj.put("joinedOn", user.getJoinedOn() != null ? user.getJoinedOn().toString() : null);
      return userObj;
    }
    throw new UnauthorizedException("login failed");
  }

  public Optional<UserModel> loadUserByUserId(String userId) {
    return this.userRepository.findById(userId);
  }
}
