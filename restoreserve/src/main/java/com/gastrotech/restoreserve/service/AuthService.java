package com.gastrotech.restoreserve.service;

import com.gastrotech.restoreserve.dto.AuthRequestDTO;
import com.gastrotech.restoreserve.dto.AuthResponseDTO;
import com.gastrotech.restoreserve.dto.RegisterRequestDTO;
import com.gastrotech.restoreserve.entity.Role;
import com.gastrotech.restoreserve.entity.User;
import com.gastrotech.restoreserve.repository.UserRepository;
import com.gastrotech.restoreserve.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public AuthResponseDTO register(RegisterRequestDTO dto) {
        if (userRepository.existsByUsername(dto.username())) {
            throw new RuntimeException("El usuario ya existe");
        }
        if (userRepository.existsByEmail(dto.email())) {
            throw new RuntimeException("El email ya está en uso");
        }

        User user = new User();
        user.setUsername(dto.username());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setEmail(dto.email());
        user.setRole(Role.ROLE_USER);

        userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(dto.username());
        String token = jwtService.generateToken(userDetails);

        return new AuthResponseDTO(token, user.getUsername(), user.getRole().name());
    }

    public AuthResponseDTO login(AuthRequestDTO dto) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(dto.username(), dto.password())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(dto.username());
        String token = jwtService.generateToken(userDetails);

        User user = userRepository.findByUsername(dto.username()).orElseThrow();

        return new AuthResponseDTO(token, user.getUsername(), user.getRole().name());
    }
}