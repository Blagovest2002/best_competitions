package com.example.model.repository;

import com.example.model.entity.Event;
import jakarta.validation.OverridesAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
@Repository
public interface EventRepository extends JpaRepository<Event,Integer> {
    List<Event> findEventsByOwnerId(int id);
    List<Event> findEventByDateAfter(Date date);
    List<Event> findEventsByLocation_City_Country_Id(int countryId);
}
