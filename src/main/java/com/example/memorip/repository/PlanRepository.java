package com.example.memorip.repository;

import com.example.memorip.entity.Plan;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface PlanRepository extends CrudRepository<Plan,Long> {
    @Override
    ArrayList<Plan> findAll();
}
