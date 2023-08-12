package com.example.service;

import com.example.jwt_configuration.JwtService;
import com.example.model.dto.EventRegisterResponseDto;
import com.example.model.dto.RegisterEventDto;
import com.example.model.entity.City;
import com.example.model.entity.Event;
import com.example.model.entity.Location;
import com.example.model.entity.User;
import com.example.model.exception.NotFoundException;
import com.example.model.exception.UnauthorizedException;
import com.example.model.repository.*;
import com.example.utility.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private LocationRepository locationRepository;
    public EventRegisterResponseDto registerEvent(RegisterEventDto registerEventDto,String token){
        //validateUser(token);
        Event event = new Event();
        City city = cityRepository.findCityById(registerEventDto.getCityId())
                .orElseThrow(()->new NotFoundException("City id is invalid"));
        Location location = new Location();
        location.setCity(city);
        locationRepository.save(location);
        event.setLocation(location);
        event.setName(registerEventDto.getEventName());
        eventRepository.save(event);
        EventRegisterResponseDto eventRegisterResponse = new EventRegisterResponseDto();
        eventRegisterResponse.setEventId(event.getId());
        return eventRegisterResponse;
    }

    private void validateUser(String token) {
        String email = jwtService.extractEmail(token);
        User user = userRepository.findUserByEmail(email).get();
        if(!user.getRole().getRole().equals(UserRole.ORGANIZER.label)){
            throw new UnauthorizedException("Only organizer can create events");
        }

    }

}

