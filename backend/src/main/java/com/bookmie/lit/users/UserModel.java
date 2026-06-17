package com.bookmie.lit.users;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.mongodb.lang.Nullable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "users")
public class UserModel {

  @Id
  private String id;

  @Indexed(unique = true)
  private String email;

  private String password;

  @Field(name = "joined_on")
  @CreatedDate
  private Instant joinedOn;

  @Field(name = "otp")
  private String otp;

  @Field(name = "last_loged_in")
  @Nullable
  private Instant lastLogedIn;

  public UserModel(String email, String password, String otp) {
    this.email = email;
    this.password = password;
    this.otp = otp;
  }
}
