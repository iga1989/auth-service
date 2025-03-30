package com.danieliga.auth_service.services;

import com.danieliga.auth_service.models.User;
import com.danieliga.auth_service.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.debug("Entering in loadUserByUsername Method...");
        User user = userRepository.findFirstByUsername(username);
        if(user == null){
            log.error("Username not found: " + username);
            throw new UsernameNotFoundException("could not found user..!!");
        }
        log.info("User Authenticated Successfully..!!!");
        return new CustomUserDetails(user);
    }
}
