package com.example.memorip.repository;

import com.example.memorip.dto.AirportDTO;
import com.example.memorip.entity.Airport;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AirportMapper {
    AirportMapper INSTANCE = Mappers.getMapper(AirportMapper.class);

    List<AirportDTO> airportsToAirportDTOs(List<Airport> airports);

    AirportDTO airportToAirportDTO(Airport airport);
}
