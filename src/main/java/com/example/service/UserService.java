package com.example.service;

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
    public User register(UserRegisterRequestDto u){
        // get the role of the user it must be 2(competitor) by default
        int roleId = u.getRoleId();
        //search for the role in db
        Optional<Role> role = roleRepository.findById(roleId);
        if (role.isEmpty()){
            throw new NotFoundException("There is not such a role");
        }
        //validate
        isValidRegistration(u);
        checkIfExists(u.getEmail(),u.getPhoneNumber());
        //validate
        //only if valid save new registered user to database
        User user = new User();
        modelMapper.map(u,user);
        user.setRole(role.get());
        userRepository.save(user);
        return user;
    }
    public void isValidRegistration(UserRegisterRequestDto user){

        if(!ValidationUtility.isValidEmail(user.getEmail())){
            throw new BadRequestException(ValidationErrorMessages.NOT_VALID_EMAIl.label);
        }
        if(!ValidationUtility.isValidPassword(user.getPassword())){
            System.out.println(user.getPassword());
            System.out.println(user.getPassword().matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$"));
            throw new BadRequestException(ValidationErrorMessages.NOT_VALID_PASSWORD.label);
        }
        if(!ValidationUtility.isValidFirstName(user.getFirstName())){
            throw new BadRequestException(ValidationErrorMessages.NOT_VALID_FIRST_NAME.label);
        }
        if(!ValidationUtility.isValidLastName(user.getLastName())){
            throw new BadRequestException(ValidationErrorMessages.NOT_VALID_LAST_NAME.label);
        }
        if(!ValidationUtility.isValidPhoneNumber(user.getPhoneNumber())){
            throw new BadRequestException(ValidationErrorMessages.NOT_VALID_PHONE_NUMBER.label);
        }
        if(!ValidationUtility.arePasswordsMatching(user.getPassword(),user.getConfirmPassword())){
            throw new BadRequestException(ValidationErrorMessages.NOT_MATCHING_PASSWORDS.label);
        }
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
