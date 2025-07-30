package com.example.poolapp.controller;

import com.example.poolapp.dto.UserResponse;
import com.example.poolapp.dto.request.AuthRequest;
import com.example.poolapp.dto.request.RegisterRequest;
import com.example.poolapp.dto.response.AuthResponse;
import com.example.poolapp.dto.response.SignupResponse;
import com.example.poolapp.exception.auth.UserAlreadyExistsException;
import com.example.poolapp.model.User;
import com.example.poolapp.repository.UserRepository;
import com.example.poolapp.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v0/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateToken(authRequest.getEmail());

        return ResponseEntity.ok(new AuthResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> registerUser(@RequestBody RegisterRequest registerRequest) {
        log.info("Start registration process");

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new UserAlreadyExistsException("Email уже используется");
        }

        log.info("RegisterUser: {}", registerRequest);

        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setName(registerRequest.getName());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        log.info("RegisterUser: {}", user);

        return ResponseEntity.ok(
                new SignupResponse(
                        "User registered successfully",
                        UserResponse.fromUser(user)
                )
        );
    }
}
