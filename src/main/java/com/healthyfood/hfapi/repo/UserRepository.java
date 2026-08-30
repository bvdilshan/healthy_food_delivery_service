package com.healthyfood.hfapi.repo;

import com.healthyfood.hfapi.model.UserModel;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepo extends MongoRepository<UserModel,String> {
    Optional<UserModel> findByEmail(String email);
}
