package com.example.memorip.service;

import com.example.memorip.entity.Plan;
import com.example.memorip.repository.PlanRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@Service
public class PlanService {
    private final PlanRepository planRepository;
    public PlanService(PlanRepository planRepository){
        this.planRepository=planRepository;
    }

    @Transactional
    public ArrayList<Plan> findAll(){
        return planRepository.findAll();
    }

    @Transactional
    public ArrayList<Plan> sortByViews(){
        return planRepository.sortByViews();
    }

    @Transactional
    public ArrayList<Plan> sortByLikes(){
        return planRepository.sortByLikes();
    }

    @Transactional
    public Plan findById(int id){
        return planRepository.findById(id);
    }

    @Transactional
    public ArrayList<Plan> findByUserId(int userId){
        return planRepository.findByUserId(userId);
    }

    @Transactional
    public Plan save(Plan entity){
        return planRepository.save(entity);
    }

    @Transactional
    public Plan deleteById(int id) {
        Optional<Plan> optionalPlan = Optional.ofNullable(planRepository.findById(id));
        if (optionalPlan.isEmpty()) {
            return null;
        }
        Plan target = optionalPlan.get();
        planRepository.delete(target);
        return target;
    }

    @Transactional
    public Plan findByUserIdAndId(int userId, int planId){
        return planRepository.findByUserIdAndId(userId,planId);
    }
}