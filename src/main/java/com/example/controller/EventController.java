package com.example.controller;

import com.example.model.dto.EventRegisterResponseDto;
import com.example.model.dto.RegisterEventDto;
import com.example.service.EventService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@AllArgsConstructor
@Data
@RequiredArgsConstructor
@RequestMapping("/event")
public class EventController extends ExceptionController{
    @Autowired
    private EventService eventService;
    @PostMapping("/add")
    public EventRegisterResponseDto registerEvent(@RequestBody RegisterEventDto registerEventDto, HttpServletRequest request)
    {
        String token = request.getHeader("Authorization").substring("Bearer ".length());
       return eventService.registerEvent(registerEventDto,token);
    }

}
