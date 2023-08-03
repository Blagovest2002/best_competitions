package com.example.service;

import com.example.model.dto.UserLoginDto;
import com.example.model.dto.UserRegisterRequestDto;
import com.example.model.entity.Role;
import com.example.model.entity.User;
import com.example.model.exception.BadRequestException;
import com.example.model.exception.NotFoundException;
import com.example.model.repository.RoleRepository;
import com.example.model.repository.UserRepository;
import com.example.utility.ValidationErrorMessages;
import com.example.utility.ValidationUtility;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ModelMapper modelMapper ;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public User register(UserRegisterRequestDto u){
        // get the role of the user it must be 2(competitor) by default
        int roleId = u.getRoleId();
        Optional<Role> role = roleRepository.findById(roleId);
        if (role.isEmpty()){
            throw new NotFoundException("There is not such a role");
        }
        ValidationUtility.isValidRegistration(u);
        checkIfExists(u.getEmail(),u.getPhoneNumber());
       User user = modelMapper.map(u,User.class);
        user.setId(0);
        user.setRole(role.get());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }
    public User login(UserLoginDto u) {
        return new User();
    }


    public void checkIfExists(String email,String phone){
        if(userRepository.findUserByEmail(email).isPresent()){
            throw new BadRequestException(ValidationErrorMessages.EMAIL_ALREADY_EXISTS.label);
        }
        if(userRepository.findUserByPhoneNumber(phone).isPresent()){
            throw new BadRequestException(ValidationErrorMessages.PHONE_NUMBER_ALREADY_EXISTS.label);
        }
    }

}
