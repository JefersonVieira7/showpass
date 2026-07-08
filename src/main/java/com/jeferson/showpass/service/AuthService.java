package com.jeferson.showpass.service;

import com.jeferson.showpass.domain.entity.User;
import com.jeferson.showpass.dto.auth.AuthResponse;
import com.jeferson.showpass.dto.auth.LoginRequest;
import com.jeferson.showpass.dto.auth.RegisterRequest;
import com.jeferson.showpass.exception.EmailAlreadyRegisteredException;
import com.jeferson.showpass.exception.InvalidCredentialsException;
import com.jeferson.showpass.repository.UserRepository;
import com.jeferson.showpass.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyRegisteredException(request.email());
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponse(token);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String token = jwtService.generateToken(user.getEmail());
        return new AuthResponse(token);
    }
}