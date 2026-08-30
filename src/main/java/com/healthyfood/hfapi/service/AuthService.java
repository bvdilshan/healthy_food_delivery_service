package com.healthyfood.hfapi.service;

import com.healthyfood.hfapi.config.JwtService;
import com.healthyfood.hfapi.dto.AuthResponse;
import com.healthyfood.hfapi.dto.LoginRequest;
import com.healthyfood.hfapi.dto.RegisterRequest;
import com.healthyfood.hfapi.model.UserModel;
import com.healthyfood.hfapi.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists!");
        }

        var user = UserModel.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() != null ? request.getRole().toUpperCase() : "USER")
                .build();

        userRepository.save(user);

        var springUser = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRole())
                .build();

        var jwtToken = jwtService.generateToken(springUser);
        return AuthResponse.builder()
                .token(jwtToken)
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        var springUser = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRole())
                .build();

        var jwtToken = jwtService.generateToken(springUser);
        return AuthResponse.builder()
                .token(jwtToken)
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}