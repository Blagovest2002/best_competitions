package com.example.controller;

import com.example.model.dto.UserRegisterRequestDto;
import com.example.model.entity.User;
import com.example.model.repository.RoleRepository;
import com.example.model.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.model.entity.Role;


@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    ModelMapper modelMapper = new ModelMapper();
    @PostMapping
    public User register(@RequestBody UserRegisterRequestDto u){
        int roleId = u.getRoleId();
        Role role = roleRepository.findById(roleId).get();
        User user = new User();
        modelMapper.map(u,user);
        user.setRole(role);
        userRepository.save(user);
        return user;
    }
}
