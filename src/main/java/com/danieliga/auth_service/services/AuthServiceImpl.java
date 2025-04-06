package com.danieliga.auth_service.services;

import com.danieliga.auth_service.dtos.AuthRequestDTO;
import com.danieliga.auth_service.dtos.JwtResponseDTO;
import com.danieliga.auth_service.models.CustomAuditEvent;
import com.danieliga.auth_service.models.User;
import com.danieliga.auth_service.repositories.DatabaseAuditEventRepository;
import com.danieliga.auth_service.repositories.UserRepository;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletRequestAttributeEvent;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;

@Data
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final AuthenticationManager authenticationManager;

    private final DatabaseAuditEventRepository auditEventRepository;

    @Override
    public String createUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
//        System.out.println("###############################");
        userRepository.save(user);
        return "User has been created successfully.";
    }

//    @Override
//    public String generateToken(String username) {
//       System.out.println("username -> "+username);
//        User user = userRepository.findFirstByUsername(username);
//        return JwtResponseDTO.builder().accessToken(jwtService.generateToken(user)).build();
//    }

    @Override
    public String generateToken(String username) {
        return jwtService.generateToken(username);
    }

    @Override
    public void validateToken(String jwtToken) {
        String username = jwtService.extractUsername(jwtToken);
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);
        jwtService.validateToken(jwtToken, userDetails);
    }

    @Override
    public JwtResponseDTO AuthenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO, HttpServletResponse response){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.username(), authRequestDTO.password()));
        String eventType = "";
        if(authentication.isAuthenticated()){
            eventType = "AUTH_SUCCESS";
            String accessToken = generateToken(authRequestDTO.username());
            // set accessToken to cookie header
            ResponseCookie cookie = ResponseCookie.from("accessToken", accessToken)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            Map<String, Object> data = new HashMap<>();
            data.put("userAgent", ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                    .getRequest().getHeader("User-Agent"));
            AuditEvent auditEvent = new AuditEvent(authRequestDTO.username(), eventType, data);
//            auditEventRepository.add(auditEvent);
            return JwtResponseDTO.builder()
                    .accessToken(accessToken).build();
        } else {
//            insertAudit(authRequestDTO);
            throw new UsernameNotFoundException("invalid user request..!!");
        }

    }

    private void insertAudit(@RequestBody AuthRequestDTO authRequestDTO) {
        String eventType;
        eventType = "AUTH_FAILURE";
        Map<String, Object> data = new HashMap<>();
        data.put("userAgent", ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest().getHeader("User-Agent"));
        AuditEvent auditEvent = new AuditEvent(authRequestDTO.username(), eventType, data);
        auditEventRepository.add(auditEvent);
    }

}
