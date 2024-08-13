package com.example.security.controller;

import com.example.security.dto.AuthDto;
import com.example.security.dto.LoginDto;
import com.example.security.dto.RegisterDto;
import com.example.security.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthService authService;

    @PostMapping(value = "login")
    public ResponseEntity<AuthDto> login(@RequestBody LoginDto login){
        AuthDto authDto = this.authService.login(login);
        return ResponseEntity.ok(authDto);
    }
    @PostMapping("/register")
    public ResponseEntity<AuthDto> register(@RequestBody RegisterDto dto){
    AuthDto authDto = this.authService.register(dto);
        return ResponseEntity.ok(authDto);
    }
}
