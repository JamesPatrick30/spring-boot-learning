package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.model.UserModel;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.exception.UserAlreadyExistsException;
@Service
public class UserService {
    UserModel newUser = new UserModel();

    public UserModel getNewUser() {
        if ( newUser.getId() == null) {
            throw new UserNotFoundException("User is null");
        }

        return newUser;
    }

    public String setNewUser(String id, String name, String email) {
        if (newUser.getId() != null) {
            throw new UserAlreadyExistsException("User already exists");
        }
        newUser.setId(id);
        newUser.setName(name);
        newUser.setEmail(email);
        return "User updated successfully";
    }
}
