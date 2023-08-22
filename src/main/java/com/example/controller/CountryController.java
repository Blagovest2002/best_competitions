package com.example.controller;

import com.example.model.entity.Country;
import com.example.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
@RestController
@RequestMapping("/countries")
public class CountryController {
    @Autowired
    CountryService countryService;
    @GetMapping("/show")
    public List<Country> getCountries(){
        return countryService.getAllCountries();

    }
}
