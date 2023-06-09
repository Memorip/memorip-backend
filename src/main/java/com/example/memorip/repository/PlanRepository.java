package com.example.memorip.repository;

import com.example.memorip.entity.Like;
import com.example.memorip.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;

public interface PlanRepository extends JpaRepository<Plan,Long> {
    @Override
    ArrayList<Plan> findAll();

    Plan findById(int id);

    @Override
    Plan save(Plan entity);

    @Query("SELECT l FROM Plan l order by l.views desc ")
    ArrayList<Plan> sortByViews();

    @Query("SELECT l FROM Plan l order by l.likes desc ")
    ArrayList<Plan> sortByLikes();

}