package com.example.controller;
import javax.validation.Valid;

import com.example.model.dto.user.*;
import com.example.model.entity.Event;
import com.example.model.exception.BadRequestException;
import com.example.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@AllArgsConstructor
@Data
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController extends ExceptionController {
    @Autowired
    private  UserService userService;
    @Autowired
    private ModelMapper modelMapper;
    @PostMapping
    public UserResponseDto register(@Valid @RequestBody UserRegisterRequestDto u){
        System.out.println("LOG");
       UserResponseDto user = userService.register(u);
        System.out.println(user.getId());
        return user;
    }
    @PostMapping("/login")
    public LoginResponseDto login(@Valid @RequestBody UserLoginDto u, BindingResult bindingResult, HttpServletRequest req){
        if(bindingResult.hasErrors()){
            throw new BadRequestException("Invalid username or password");
        }
        LoginResponseDto user = userService.login(u);
        return user;

    }
   /* @GetMapping("/info/{id}")
    public LoginResponseDto info(@PathVariable("id") int id){
        System.out.println("INFO LOG");
        //LoginResponseDto user = userService.getInfo(id);
        return user;
    }*/
    @PostMapping("/logout")//todo make the logic for deletion of tokens in blacklist
    public void logout(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring("Bearer ".length());
        userService.logout(token);
        //todo blacklist for tokens
    }
    @GetMapping("/profile")
    public UserInfoDto getProfileInfo(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null){
            throw new BadRequestException("Expected jwt token for authentication");
        }
        String token = authHeader.substring("Bearer ".length());
        UserInfoDto user = userService.getProfileInfo(token);
        return user;
    }
    @GetMapping("/info/{id}")
    public ShowUserDto showUserInformation(@PathVariable("id") int id){
        ShowUserDto showUserDto  = userService.showUser(id);
        return showUserDto;
    }
    //todo edit profile
    //todo change passwords
    //show organizers events
    @GetMapping("/events")
    public List<Event> showOrganizerEvents(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring("Bearer ".length());
        List<Event> events  = userService.showOrganizerEvents(token);
        return events;
    }
    //todo register for event
    @PostMapping("/event/{id}/register")
    public RegisterUserForEventResponseDto registerUserForEvent(
            @RequestBody RegisterUserForEventDto registerUserForEventDto,
            @PathVariable("id") int id,HttpServletRequest request){
        String token = request.getHeader("Authorization").substring("Bearer ".length());
       return userService.registerUserForEvent(registerUserForEventDto.getWeightClassId(),id,token);
    }
    //todo show all upcoming events for user
    //todo show all past events in which user participated

}
