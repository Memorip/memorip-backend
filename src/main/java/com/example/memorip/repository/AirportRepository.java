package com.example.memorip.repository;

import com.example.memorip.entity.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AirportRepository extends JpaRepository<Airport, Integer> {
    @Query("SELECT a FROM Airport a " +
            "WHERE a.englishName LIKE %:keyword% " +
            "OR a.koreanName LIKE %:keyword% " +
            "OR a.city LIKE %:keyword% " +
            "OR a.code LIKE %:keyword% ")
    List<Airport> findByKeyword(String keyword);
}
