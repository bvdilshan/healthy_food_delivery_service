package com.healthyfood.hfapi.controller;

import com.healthyfood.hfapi.dto.UserRequest;
import com.healthyfood.hfapi.dto.UserResponse;
import com.healthyfood.hfapi.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody UserRequest userRequest) {
        UserResponse response = userService.registerUser(userRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}