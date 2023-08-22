package com.example.controller;

import com.example.model.dto.event.EventRegisterResponseDto;
import com.example.model.dto.event.RegisterEventDto;
import com.example.model.dto.event.ShowEventDto;
import com.example.model.dto.event.WeightClassCreationDto;
import com.example.model.dto.participant.ShowParticipantDto;
import com.example.model.entity.Participant;
import com.example.model.entity.WeightClass;
import com.example.service.EventService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.OverridesAttribute;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@AllArgsConstructor
@Data
@RequiredArgsConstructor
@RequestMapping("/event")
public class EventController extends ExceptionController{
    @Autowired
    private EventService eventService;
    //add event
    @PostMapping("/add")
    public EventRegisterResponseDto registerEvent(@RequestBody RegisterEventDto registerEventDto, HttpServletRequest request)
    {
        String token = request.getHeader("Authorization").substring("Bearer ".length());
       return eventService.registerEvent(registerEventDto,token);
    }
    //show event by id
    @GetMapping("/{id}")
    public ShowEventDto showEvent(@PathVariable("id") int id){
        ShowEventDto showEventDto = eventService.showEvent(id);
        return showEventDto;
    }
    @PostMapping("/{id}/weight_classes/add")
    public void addWeightClass(@RequestBody WeightClassCreationDto weightClassCreationDto,
                               @PathVariable("id") int id,HttpServletRequest request){
        String token = request.getHeader("Authorization").substring("Bearer ".length());
        eventService.addWeightClass(weightClassCreationDto.getWeightClass(),id,token);
    }
    @GetMapping("/{id}/weight_classes")
    public List<WeightClass> showWeightClassesForEvent(@PathVariable("id") int id){
        List<WeightClass> weightClasses;
        weightClasses = eventService.getWeightClassesForEvent(id);
        return weightClasses;
        }
        @GetMapping("/{event_id}/weight_class/{weight_class_id}/participants")
        public List<ShowParticipantDto> showParticipantsForCategory(@PathVariable("event_id") int eventId,
                                                                    @PathVariable("weight_class_id") int weightClassId){
               return eventService.showParticipantsForCategory(eventId,weightClassId);

        }


    //todo show all upcoming events
    @GetMapping("/upcoming")
    public List<ShowEventDto> showUpcomingEventsDto(){
        return eventService.showAllUpcomingEvents();
    }
    //todo show all upcoming events in given country
    @GetMapping("/upcoming/country")
    public List<ShowEventDto> showUpcomingEventsInCountry(@RequestParam(name = "country_id") int countryId){
        return eventService.showUpcomingEventsInCountry(countryId);

    }
}
