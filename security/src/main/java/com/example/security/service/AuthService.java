package com.example.security.service;

import com.example.security.config.JwtService;
import com.example.security.dto.AuthDto;
import com.example.security.dto.LoginDto;
import com.example.security.dto.RegisterDto;
import com.example.security.entity.UserEntity;
import com.example.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    public AuthDto login(final LoginDto request) {
        // Autenticación del usuario
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // Cargar detalles del usuario
        UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());

        // Generar token JWT
        String token = jwtService.getToken(user);

        return new AuthDto(token);
    }

    public AuthDto register(final RegisterDto request) {
        // Verifica si el usuario ya existe
        Optional<UserEntity> existingUser = userRepository.findByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Usuario ya registrado");
        }

        // Crear nuevo usuario
        UserEntity user = new UserEntity();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(); // Asignar un rol por defecto o según necesidad

        // Guardar usuario en el repositorio
        userRepository.save(user);

        // Generar token JWT
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtService.getToken(userDetails);

        return new AuthDto(token);
    }
}
