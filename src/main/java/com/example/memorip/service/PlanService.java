package com.example.memorip.service;

import com.example.memorip.entity.Plan;
import com.example.memorip.repository.PlanRepository;
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
}
