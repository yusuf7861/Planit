package com.yusuf.planit.services;

import com.yusuf.planit.entities.User;
import com.yusuf.planit.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    Instant currentTime = Instant.now();


    public void registerUser(User user)
    {
        if(userRepository.findByEmail(user.getEmail()).isPresent())
        {
            logger.error("User with email: {} already exists", user.getEmail());
            throw new IllegalArgumentException("Email already exists.");
        }
        else
        {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setCreatedAt(currentTime);
            userRepository.save(user);
            logger.info("User with email: {} registered successfully", user.getEmail());
            User savedUser = userRepository.save(user);
            logger.info("New User registered: ID={} Email={}", savedUser.getId(), savedUser.getEmail());
        }
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    public User findByUserId(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with userId: " + userId));
    }
}
