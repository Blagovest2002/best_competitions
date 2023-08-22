package com.example.service;

import com.example.model.entity.Country;
import com.example.model.repository.CountryRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Service
public class CountryService {
    @Autowired
    private CountryRepository countryRepository;
    public List<Country> getAllCountries(){
        List<Country> countries  = countryRepository.findAll();
        return countries;
    }
    /* public List<String> getAllCountries(){
        List<Country> countries  = countryRepository.findAll();
        return countries.stream().map(Country::getCountryName).collect(Collectors.toList());
    }
*/
}
