package com.example.memorip.repository;

import com.example.memorip.entity.Timeline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.Optional;

public interface TimelineRepository extends JpaRepository<Timeline,Long>{
    @Query("SELECT t FROM Timeline t WHERE t.plan.id = :planId ORDER BY t.date")
    ArrayList<Timeline> findAllByPlanId(int planId);

    Optional<Timeline> findById(int id);
    void deleteById(int id);
}
