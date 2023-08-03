package com.example.controller;
import javax.validation.Valid;

import com.example.model.dto.UserLoginDto;
import com.example.model.dto.UserRegisterRequestDto;
import com.example.model.dto.UserResponseDto;
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
    @Autowired
    private ModelMapper modelMapper;
    @PostMapping
    public UserResponseDto register(@Valid @RequestBody UserRegisterRequestDto u){
        System.out.println("LOG");
       User user = userService.register(u);
        System.out.println(user.getId());
        return modelMapper.map(user,UserResponseDto.class);
    }
    @PostMapping("/login")
    public UserResponseDto login(@Valid @RequestBody UserLoginDto u){
        User user = userService.login(u);

        return modelMapper.map(user,UserResponseDto.class);

    }
}
