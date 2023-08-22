package com.example.service;

import com.example.jwt_configuration.JwtService;
import com.example.model.dto.event.EventRegisterResponseDto;
import com.example.model.dto.event.RegisterEventDto;
import com.example.model.dto.event.ShowEventDto;
import com.example.model.dto.participant.ShowParticipantDto;
import com.example.model.entity.*;
import com.example.model.exception.BadRequestException;
import com.example.model.exception.NotFoundException;
import com.example.model.exception.UnauthorizedException;
import com.example.model.repository.*;
import com.example.utility.UserRole;
import jakarta.validation.OverridesAttribute;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private CategoryRepository categoryRepository;
    public EventRegisterResponseDto registerEvent(RegisterEventDto registerEventDto,String token){
        //validateUser(token);
        checkDateOfEvent(registerEventDto.getDate());
        Event event = new Event();
        City city = cityRepository.findCityById(registerEventDto.getCityId())
                .orElseThrow(()->new NotFoundException("City id is invalid"));
        Location location = new Location();
        User user = userRepository.findUserByEmail(jwtService.extractEmail(token)).orElseThrow(()-> new NotFoundException("user is not found"));
        location.setCity(city);
        location.setAddress(registerEventDto.getAddress());
        locationRepository.save(location);
        event.setLocation(location);
        event.setName(registerEventDto.getEventName());
        event.setOwner(user);
        event.setCompleted(false);

        event.setDate(java.sql.Date.valueOf(registerEventDto.getDate()));

        event.setOpenForRegistrations(true);
        eventRepository.save(event);
        EventRegisterResponseDto eventRegisterResponse = new EventRegisterResponseDto();
        eventRegisterResponse.setEventId(event.getId());
        return eventRegisterResponse;
    }

    private void checkDateOfEvent(LocalDate date) {
        if(date.isBefore(LocalDate.now())){
            throw new BadRequestException("Date must be in the future");
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
        showEventDto.setId(id);
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

    public List<ShowParticipantDto> showParticipantsForCategory(int eventId, int weightClassId) {
       Category category = categoryRepository.findByWeightClassId(weightClassId).orElseThrow(
               ()-> new NotFoundException("Category not found!")
       );
        List<Participant> participants = category.getParticipants();
       List<ShowParticipantDto> participantsDto = new ArrayList<>();
       for(Participant participant : participants){
           ShowParticipantDto participantDto = new ShowParticipantDto();
           participantDto.setFirstName(participant.getUser().getFirstName());
            participantDto.setLastName(participant.getUser().getLastName());
            participantDto.setWeightClass(category.getWeightClass());
            participantDto.setId(participant.getId());
            participantsDto.add(participantDto);
       }
       return participantsDto;
    }

    public List<ShowEventDto> showAllUpcomingEvents() {
        List<Event> events = eventRepository.findEventByDateAfter(Date.valueOf(LocalDate.now()));
        List<ShowEventDto> dtoEvents = new ArrayList<>();
        for(Event event : events){
            ShowEventDto showEventDto = new ShowEventDto();
            showEventDto.setId(event.getId());
            showEventDto.setCity(event.getLocation().getCity().getCityName());
            showEventDto.setName(event.getName());
            showEventDto.setCountry(event.getLocation().getCity().getCountry().getCountryName());
            dtoEvents.add(showEventDto);
        }
        return dtoEvents;
    }

    public List<ShowEventDto> showUpcomingEventsInCountry(int countryId) {
        return eventRepository.findEventsByLocation_City_Country_Id(countryId)
                .stream()
                .map((Event e)->{
                    ShowEventDto eventDto = new ShowEventDto();
                    eventDto.setId(e.getId());
                    eventDto.setCountry(e.getLocation().getCity().getCountry().getCountryName());
                    eventDto.setName(e.getName());
                    eventDto.setCity(e.getLocation().getCity().getCityName());
                    return eventDto;
                }).collect(Collectors.toList());
    }
}

