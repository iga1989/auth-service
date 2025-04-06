package com.danieliga.auth_service.services;

import com.danieliga.auth_service.models.User;
import com.danieliga.auth_service.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Component
public class JwtService {

    private final UserRepository userRepository;

    public static final String SECRET = "357638792F423F4428472B4B6250655368566D597133743677397A2443264629";

    @Value("${jwt.expiryDate}")
    public int expiryDate;

    public JwtService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

//    public String generateToken(User user){
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("roles", user.getRoles());
//        return createToken(claims, user.getUsername());
//    }

    public String generateToken(String username){
        Map<String, Object> claims = new HashMap<>();
        User user = userRepository.findFirstByUsername(username);
        claims.put("roles", user.getRoles());
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String username) {
        Date now = new Date();
        Date expiredAt = new Date(now.getTime() + expiryDate*1000L);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expiredAt)
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
