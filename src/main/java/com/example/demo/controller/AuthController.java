package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.LoginDTO;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import com.example.demo.exception.UnauthorizedException;

import com.example.demo.service.JwtService;
import com.example.demo.model.LoginResponseModel;
import com.example.demo.model.TokenModel;

@RequestMapping("/auth")
@RestController
public class AuthController {

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public LoginResponseModel postMethodName(@Valid @RequestBody LoginDTO entity) {
        //TODO: process POST request
        System.out.println("Email: " + entity.getEmail());

        TokenModel tokenModel = new TokenModel();
        tokenModel.setEmail(entity.getEmail());
        tokenModel.setName("John Doe");
        tokenModel.setId("1234");

        LoginResponseModel response = new LoginResponseModel();
        response.setAccessToken(jwtService.generateAccessToken(tokenModel));
        response.setRefreshToken(jwtService.generateRefreshToken(tokenModel));
        return response;
    }

    @PostMapping("/refresh")
    public LoginResponseModel postMethodName(@RequestHeader("Authorization") String refreshToken) {
        //TODO: process POST request
        
        TokenModel tokenModel = new TokenModel();
        tokenModel = jwtService.isRefreshTokenValid(refreshToken);
        if (tokenModel != null) {
            System.out.println("Refresh token is valid for user: " + tokenModel.getEmail());
            LoginResponseModel response = new LoginResponseModel();
            response.setAccessToken(jwtService.generateAccessToken(tokenModel));
            response.setRefreshToken(jwtService.generateRefreshToken(tokenModel));
            return response;
        } else {
            // Handle invalid token case
            throw new UnauthorizedException("Invalid refresh token");
        }
    }
    
    
}