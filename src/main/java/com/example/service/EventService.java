package com.example.service;

import com.example.jwt_configuration.JwtService;
import com.example.model.dto.EventRegisterResponseDto;
import com.example.model.dto.RegisterEventDto;
import com.example.model.dto.ShowEventDto;
import com.example.model.entity.*;
import com.example.model.exception.NotFoundException;
import com.example.model.exception.UnauthorizedException;
import com.example.model.repository.*;
import com.example.utility.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    @Autowired
    private WeightClassRepository weightClassRepository;
    public EventRegisterResponseDto registerEvent(RegisterEventDto registerEventDto,String token){
        //validateUser(token);
        Event event = new Event();
        City city = cityRepository.findCityById(registerEventDto.getCityId())
                .orElseThrow(()->new NotFoundException("City id is invalid"));
        Location location = new Location();
        User user = userRepository.findUserByEmail(jwtService.extractEmail(token)).orElseThrow(()-> new NotFoundException("user is not found"));
        location.setCity(city);
        locationRepository.save(location);
        event.setLocation(location);
        event.setName(registerEventDto.getEventName());
        event.setOwner(user);
        event.setCompleted(false);
        event.setOpenForRegistrations(true);
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

    public ShowEventDto showEvent(int id) {
        Event event = eventRepository.findById(id).orElseThrow(()->new NotFoundException("This event does not exist!"));
        String city = event.getLocation().getCity().getCityName();
        String country = event.getLocation().getCity().getCountry().getCountryName();
        ShowEventDto showEventDto = new ShowEventDto();
        showEventDto.setName(event.getName());
        showEventDto.setCountry(country);
        showEventDto.setCity(city);
        return showEventDto;
    }

    public void addWeightClass(int weightClass,int eventId,String token) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(()->new NotFoundException("There is not such event"));
        checkIfEventBelongsToUser(eventId,token);
        WeightClass weightClassEntity = new WeightClass();
        weightClassEntity.setWeightClass(weightClass);
        weightClassEntity.setEvent(event);
        weightClassRepository.save(weightClassEntity);
    }

    private void checkIfEventBelongsToUser(int eventId, String token) {
        User user = userRepository.findUserByEmail(jwtService.extractEmail(token))
                .orElseThrow(()->new NotFoundException("User is not found!"));
        boolean doesEventBelongToUser = false;
        for(Event e: user.getEvents()){
            if(e.getId() == eventId){
                doesEventBelongToUser = true;
                break;
            }
        }
        if(!doesEventBelongToUser){
            throw new UnauthorizedException("The user is not owner of this event!");
        }
    }

    public List<WeightClass> getWeightClassesForEvent(int id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Event not found!"));
        return event.getWeightClasses();
    }
}

