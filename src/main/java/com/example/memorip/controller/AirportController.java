package com.example.memorip.controller;

import com.example.memorip.dto.AirportDTO;
import com.example.memorip.exception.DefaultRes;
import com.example.memorip.repository.AirportMapper;
import com.example.memorip.service.AirportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Tag(name = "airport", description = "airport api 입니다.")
@RestController
@RequestMapping("/api/airports")
@Validated
public class AirportController {

    private final AirportService airportService;

    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    @Operation(summary = "공항 검색", description = "공항명, 도시명, 공항코드(IATA)로 검색된 공항목록 결과를 리턴합니다.")
    @GetMapping("/search")
    public ResponseEntity<DefaultRes<List<AirportDTO>>> searchAirport(@RequestParam("keyword") String keyword) {
        List<AirportDTO> airportDTOs = AirportMapper.INSTANCE.airportsToAirportDTOs(airportService.searchAirport(keyword));

        return new ResponseEntity<>(DefaultRes.res(200, "공항 검색 성공", airportDTOs), HttpStatus.OK);
    }
}
