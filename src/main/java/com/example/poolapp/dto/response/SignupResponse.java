package com.example.poolapp.dto.response;

import com.example.poolapp.dto.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SignupResponse {
    private String message;
    private UserResponse user;
}
