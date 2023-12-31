package com.example.service;

import com.example.jwt_configuration.JwtService;
import com.example.model.dto.user.*;
import com.example.model.entity.*;
import com.example.model.exception.BadRequestException;
import com.example.model.exception.NotFoundException;
import com.example.model.exception.UnauthorizedException;
import com.example.model.repository.*;
import com.example.utility.ValidationErrorMessages;
import com.example.utility.ValidationUtility;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.parameters.P;
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
    private CategoryRepository categoryRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private WeightClassRepository weightClassRepository;
    @Autowired
    private ParticipantRepository participantRepository;
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
        Token userToken = tokenRepository.findTokenByToken(token);
        userToken.setIsExpired("true");
        userToken.setIsRevoked("true");
        tokenRepository.save(userToken);
    }

    public ShowUserDto showUser(int id) {
        User user = userRepository.findUserById(id).orElseThrow(()->new UnauthorizedException("This user does not exists!"));
        ShowUserDto showUserDto = new ShowUserDto();
        showUserDto.setAge(user.getAge());
        showUserDto.setFirstName(user.getFirstName());
        showUserDto.setLastName(user.getLastName());
        showUserDto.setRole(user.getRole());
        return  showUserDto;
    }

    public List<Event> showOrganizerEvents(String token) {
        User user = userRepository.findUserByEmail(jwtService.extractEmail(token))
                .orElseThrow(()->new NotFoundException("There is not such User!"));
        System.out.println("USER IS FOUND" + user.getFirstName());
        List<Event> events = eventRepository.findEventsByOwnerId(user.getId());
        System.out.println("EVENTS ARWE FOUND + : " + events.size());
       // System.out.println(events);
        return events;
    }
//todo validate whether the user is already registered for the event

    public RegisterUserForEventResponseDto registerUserForEvent(int weightClassId, int eventId,String token) {

        User user = userRepository.findUserByEmail(jwtService.extractEmail(token)).get();
        Event event = eventRepository.findById(eventId).orElseThrow(
                ()->new NotFoundException("The event does not exist!"));
        System.out.println("USER EVENTS SHOW : ");
        System.out.println(user.getEvents());
        System.out.println(event);
        if(user.getEvents().contains(event)){
            throw new BadRequestException("The user is already registered!");
        }
        if(!event.isOpenForRegistrations()){
            throw new BadRequestException("The event is closed for registration");
        }
        WeightClass weightClass = weightClassRepository
                .findWeightClassById(weightClassId)
                .orElseThrow(
                ()->new NotFoundException("The weight class does not exists!")
                );
        if(weightClass.getEvent().getId()!= eventId){
            throw new BadRequestException("this weight_class_id is not for this event");
        }
        if(participantRepository.findParticipantByUserIdAndEventId(user.getId(),eventId)!=null){
            throw new BadRequestException("this user is already registered!");
        }
        Category category = categoryRepository
                .findByWeightClassId(weightClassId)
                .orElseGet(()->new Category());

        category.setEvent(event);
        category.setWeightClass(weightClass);
        categoryRepository.save(category);

        Participant participant = new Participant();
        participant.setCategory(category);
        participant.setEvent(event);
        participant.setUser(user);
        participantRepository.save(participant);
       //form the dto
        RegisterUserForEventResponseDto registerUserForEventResponseDto = new RegisterUserForEventResponseDto();
        registerUserForEventResponseDto.setFirstName(user.getFirstName());
        registerUserForEventResponseDto.setLastName(user.getLastName());
        registerUserForEventResponseDto.setWeightClass(weightClass.getWeightClass());
        return registerUserForEventResponseDto;
    }
}
