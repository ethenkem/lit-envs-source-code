package com.bookmie.gitforenv.auths.repos;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bookmie.gitforenv.auths.models.UserModel;

@Repository
public interface UserRepository extends MongoRepository<UserModel, String> {
  Optional<UserModel> findByEmail(String email);
}
