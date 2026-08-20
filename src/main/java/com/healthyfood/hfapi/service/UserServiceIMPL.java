package com.healthyfood.hfapi.service;

import com.healthyfood.hfapi.dto.UserRequest;
import com.healthyfood.hfapi.dto.UserResponse;
import com.healthyfood.hfapi.model.UserModel;
import com.healthyfood.hfapi.repo.UserRepo;
import org.springframework.stereotype.Service;

@Service
public class UserServiceIMPL implements UserService {
    private final UserRepo userRepo;

    public UserServiceIMPL(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserResponse registerUser(UserRequest request) {
        UserModel userModel = convertToUserModel(request);
        UserModel savedUser = userRepo.save(userModel);
        return convertToUserResponse(savedUser);
    }

    private UserModel convertToUserModel(UserRequest userRequest) {
        return UserModel.builder()
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .name(userRequest.getName())
                .build();
    }

    private UserResponse convertToUserResponse(UserModel userModel) {
        return UserResponse.builder()
                .id(userModel.getId())
                .email(userModel.getEmail())
                .name(userModel.getName())
                .build();
    }
}