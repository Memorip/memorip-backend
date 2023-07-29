package com.example.memorip.repository;

import com.example.memorip.entity.Travel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;

public interface TravelRepository extends JpaRepository<Travel,Integer> {
    @Override
    ArrayList<Travel> findAll();

    @Override
    Travel save(Travel entity);

    @Query("SELECT l FROM Travel l where l.user.id = :userId")
    ArrayList<Travel> findByUserId(int userId);
}
