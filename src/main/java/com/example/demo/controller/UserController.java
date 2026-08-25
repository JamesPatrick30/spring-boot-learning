package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CreateUserDto;
import com.example.demo.model.UserModel;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.demo.service.UserService;


@RequestMapping("/user")
@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public String postMethodName(@Valid @RequestBody CreateUserDto user) {
        String id = "1234";
        String name = user.getName();
        String email = user.getEmail();
        return userService.setNewUser(id, name, email);


    }

    @GetMapping
    public UserModel getMethodName() {
        return userService.getNewUser();
    } 

}

    
