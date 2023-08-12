package com.example.service;

import com.example.jwt_configuration.JwtService;
import com.example.model.dto.*;
import com.example.model.entity.Role;
import com.example.model.entity.Token;
import com.example.model.entity.User;
import com.example.model.exception.BadRequestException;
import com.example.model.exception.NotFoundException;
import com.example.model.repository.RoleRepository;
import com.example.model.repository.TokenRepository;
import com.example.model.repository.UserRepository;
import com.example.utility.ValidationErrorMessages;
import com.example.utility.ValidationUtility;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ModelMapper modelMapper ;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    public UserResponseDto register(UserRegisterRequestDto u){
        System.out.println("get here register");
        // get the role of the user it must be 2(competitor) by default
        int roleId = u.getRoleId();
        Optional<Role> role = roleRepository.findById(roleId);
        if (role.isEmpty()){
            throw new NotFoundException("There is not such a role");
        }
        ValidationUtility.isValidRegistration(u);
        checkIfExists(u.getEmail(),u.getPhoneNumber());
        //todo make it with builder pattern
       User user = modelMapper.map(u,User.class);
        user.setId(0);
        user.setRole(role.get());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);
        UserResponseDto userResponseDto = UserResponseDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .token(jwtToken).build();

        return userResponseDto;
    }
    public LoginResponseDto login(UserLoginDto u) {
       /* authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        u.getEmail(),
                        u.getPassword())
        );*/
        User user = userRepository.findUserByEmail(u.getEmail()).orElseThrow(() -> new NotFoundException("Username Not Found"));
        String token = jwtService.generateToken(user);
        Token tokеnEntity = new Token();
        tokеnEntity.setIsExpired("false");
        tokеnEntity.setIsRevoked("false");
        tokеnEntity.setToken(token);
        tokеnEntity.setUser(user);

        List<Token> userTokens = tokenRepository.findTokenByUserId(user.getId());
        for(Token userToken:userTokens){
            userToken.setIsRevoked("true");
            userToken.setIsExpired("true");
            tokenRepository.save(userToken);
        }
        tokenRepository.save(tokеnEntity);
        LoginResponseDto loginResponseDto = LoginResponseDto.builder()
                .token(token)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .id(user.getId()).build();
        return loginResponseDto;

    }
    public LoginResponseDto getInfo(String token){
       User u =  userRepository.findUserById(2).orElseThrow(()->new NotFoundException("User does not exist!"));
       return LoginResponseDto.builder()
               .id(2)
               .firstName(u.getFirstName())
               .lastName(u.getLastName())
               .token(token)
               .build();

    }


    public void checkIfExists(String email,String phone){
        if(userRepository.findUserByEmail(email).isPresent()){
            throw new BadRequestException(ValidationErrorMessages.EMAIL_ALREADY_EXISTS.label);
        }
        if(userRepository.findUserByPhoneNumber(phone).isPresent()){
            throw new BadRequestException(ValidationErrorMessages.PHONE_NUMBER_ALREADY_EXISTS.label);
        }
    }


    public UserInfoDto getProfileInfo(String token) {
        String email  = jwtService.extractEmail(token);
        User user = userRepository.findUserByEmail(email).orElseThrow(()->new NotFoundException("User not found!"));
        UserInfoDto userInfoDto = UserInfoDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build();
        return userInfoDto;
    }

    public void logout(String token) {
        Token userToken = tokenRepository.findTokenByToken("token");
        userToken.setIsExpired("true");
        userToken.setIsRevoked("true");
        tokenRepository.save(userToken);
    }
}
