package com.example.model.repository;

import com.example.model.entity.WeightClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeightClassRepository extends JpaRepository<WeightClass,Integer> {
}
