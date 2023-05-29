package com.example.memorip.service;

import com.example.memorip.controller.PlanController;
import com.example.memorip.entity.Plan;
import com.example.memorip.repository.PlanRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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
        return planRepository.findById(id);
    }

    public Plan save(Plan entity){
        return planRepository.save(entity);
    }

    public Plan deleteById(int id){
        Plan target = planRepository.findById(id);

        if(target == null){
            return null;
        }
        planRepository.deleteById(id);
        return target;
    }
}
