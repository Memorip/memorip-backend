package com.example.memorip.service;

import com.example.memorip.entity.Airport;
import com.example.memorip.repository.AirportRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirportService {

    private final AirportRepository airportRepository;

    public AirportService(AirportRepository airportRepository) {
        this.airportRepository = airportRepository;
    }

    public List<Airport> searchAirport(String keyword) {
        return airportRepository.findByKeyword(keyword);
    }
}
