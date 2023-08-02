package com.example.controller;

import com.example.model.dto.UserRegisterRequestDto;
import com.example.model.entity.User;
import com.example.model.repository.RoleRepository;
import com.example.model.repository.UserRepository;
import com.example.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users")
public class UserController extends ExceptionController {
    @Autowired
    private UserService userService;
    @PostMapping
    public User register(@RequestBody UserRegisterRequestDto u){
       User user = userService.register(u);
        return user;
    }
}
