package com.example.memorip.repository;

import com.example.memorip.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan,Integer> {

    @Query("SELECT l FROM Plan l where l.isPublic = true")
    ArrayList<Plan> getAllPlan();

    @Query("SELECT l FROM Plan l where l.user.id = :userId")
    ArrayList<Plan> findByUserId(int userId);

    @Override
    Plan save(Plan entity);

    @Query("SELECT l FROM Plan l where l.isPublic = true order by l.views desc ")
    ArrayList<Plan> sortByViews();

    @Query("SELECT l FROM Plan l where l.isPublic = true  order by l.likes desc ")
    ArrayList<Plan> sortByLikes();

    Optional<Plan> findByUserIdAndId(int userId, int planId);
}