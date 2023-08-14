package com.example.model.repository;

import com.example.model.entity.WeightClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeightClassRepository extends JpaRepository<WeightClass,Integer> {
    Optional<WeightClass> findWeightClassById(int id);
}
