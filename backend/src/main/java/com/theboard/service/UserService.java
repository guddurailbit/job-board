package com.theboard.service;


import com.theboard.Security.JwtUtil;
import com.theboard.dto.LoginRequest;
import com.theboard.dto.LoginResponse;
import com.theboard.dto.RegisterRequest;
import com.theboard.model.User;
import com.theboard.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // ===========================
    // Register
    // ===========================

    public String register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered.");
        }

        User user = new User();

        user.setName(request.getName());

        user.setEmail(request.getEmail());

        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        user.setRole(request.getRole());

        user.setCompanyName(request.getCompanyName());

        userRepository.save(user);

        return "Registration Successful";
    }

    // ===========================
    // Login
    // ===========================

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())

                .orElseThrow(() ->
                        new RuntimeException("Invalid Email or Password"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new RuntimeException("Invalid Email or Password");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        return new LoginResponse(

                token,

                user.getName(),

                user.getEmail(),

                user.getRole().name()

        );
    }

}