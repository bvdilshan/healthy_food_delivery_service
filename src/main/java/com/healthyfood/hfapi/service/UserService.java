package com.healthyfood.hfapi.service;

import com.healthyfood.hfapi.dto.UserRequest;
import com.healthyfood.hfapi.dto.UserResponse;

public interface UserService {
    UserResponse registerUser(UserRequest request);
}