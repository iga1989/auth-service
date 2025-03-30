package com.danieliga.auth_service.services;

import com.danieliga.auth_service.dtos.JwtResponseDTO;
import com.danieliga.auth_service.models.User;
import com.danieliga.auth_service.repositories.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Data
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Override
    public String createUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
//        System.out.println("###############################");
        userRepository.save(user);
        return "User has been created successfully.";
    }

    @Override
    public JwtResponseDTO generateToken(String username) {
//        System.out.println("username -> "+username);
        User user = userRepository.findFirstByUsername(username);
        return JwtResponseDTO.builder().accessToken(jwtService.generateToken(user)).build();
    }

    @Override
    public void validateToken(String jwtToken) {
        String username = jwtService.extractUsername(jwtToken);
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
        jwtService.validateToken(jwtToken, userDetails);
    }
}
