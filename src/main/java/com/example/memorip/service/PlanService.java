package com.example.memorip.service;

import com.example.memorip.controller.PlanController;
import com.example.memorip.entity.Plan;
import com.example.memorip.repository.PlanRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@Service
public class PlanService {
    private final PlanRepository planRepository;
    private PlanService(PlanRepository planRepository){
        this.planRepository=planRepository;
    }

    public ArrayList<Plan> selectAll(){
        return planRepository.findAll();
    }

    public Plan findById(int id){
        Plan plan = planRepository.findById(id);
        if(plan==null) return null;
        return planRepository.findById(id);
    }

    public Plan save(Plan entity){
        return planRepository.save(entity);
    }

    public Plan deleteById(int id) {
        Optional<Plan> optionalPlan = Optional.ofNullable(planRepository.findById(id));
        if (optionalPlan.isEmpty()) {
            return null;
        }
        Plan target = optionalPlan.get();
        planRepository.delete(target);
        return target;
    }
}
