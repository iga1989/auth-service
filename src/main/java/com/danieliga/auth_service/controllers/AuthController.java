package com.danieliga.auth_service.controllers;

import com.danieliga.auth_service.dtos.AuthRequestDTO;
import com.danieliga.auth_service.dtos.JwtResponseDTO;
import com.danieliga.auth_service.models.User;
import com.danieliga.auth_service.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> createUser(@RequestBody User user){
        try{
            return ResponseEntity.ok(authService.createUser(user));
        } catch (Exception ex){
            throw new RuntimeException(ex.getMessage());
        }
    }

    @PostMapping("/generateToken")
    public ResponseEntity<JwtResponseDTO> generateToken(@RequestBody AuthRequestDTO authRequestDTO){
        try{
            System.out.println("authRequestDTO -> "+authRequestDTO.toString());
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));
            if(authentication.isAuthenticated()){
                return ResponseEntity.ok(authService.generateToken(authRequestDTO.getUsername()));
            } else {
                throw new RuntimeException("Invalid User Request..!");
            }

        } catch (Exception ex){
            throw new RuntimeException(ex.getMessage());
        }
    }

    @GetMapping("/validateToken")
    public String validateToken(@RequestParam String jwtToken){
        try{
            authService.validateToken(jwtToken);
            return "Token is valid";
        } catch (Exception ex){
            throw new RuntimeException(ex.getMessage());
        }
    }

}