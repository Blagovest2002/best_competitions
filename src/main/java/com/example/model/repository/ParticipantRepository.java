package com.example.model.repository;

import com.example.model.entity.Participant;
import com.example.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant,Integer> {
    Participant findParticipantByUserIdAndEventId(int userId,int eventId);
}
