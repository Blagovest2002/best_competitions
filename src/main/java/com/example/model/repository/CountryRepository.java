package com.example.model.repository;

import com.example.model.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CountryRepository extends JpaRepository<Country,Integer> {
    Optional<Country> findCountryById(int id);
    List<Country> findAll();
}
