package com.danieliga.auth_service.services;

import com.danieliga.auth_service.dtos.AuthRequestDTO;
import com.danieliga.auth_service.dtos.JwtResponseDTO;
import com.danieliga.auth_service.models.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthService {
    String createUser(User user);
    String generateToken(String username);
    void validateToken(String jwtToken);
    JwtResponseDTO AuthenticateAndGetToken(AuthRequestDTO authRequestDTO, HttpServletResponse response);

}