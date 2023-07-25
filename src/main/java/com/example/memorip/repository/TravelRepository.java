package com.example.memorip.repository;

import com.example.memorip.entity.Travel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface TravelRepository extends JpaRepository<Travel,Long> {
    @Override
    ArrayList<Travel> findAll();

    Travel findById(int id);

    @Override
    Travel save(Travel entity);


    Travel findByUserIdAndPlanId(int userId, int planId);
}
