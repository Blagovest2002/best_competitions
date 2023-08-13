package com.example.model.repository;

import com.example.model.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface EventRepository extends JpaRepository<Event,Integer> {
    List<Event> findEventsByOwnerId(int id);
}
