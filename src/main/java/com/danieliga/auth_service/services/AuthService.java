package com.danieliga.auth_service.services;

import com.danieliga.auth_service.dtos.JwtResponseDTO;
import com.danieliga.auth_service.models.User;

public interface AuthService {
    String createUser(User user);
    JwtResponseDTO generateToken(String username);
    void validateToken(String jwtToken);
}