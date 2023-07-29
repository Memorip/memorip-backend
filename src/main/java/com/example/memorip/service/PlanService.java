package com.example.memorip.service;

import com.example.memorip.entity.Plan;
import com.example.memorip.exception.CustomException;
import com.example.memorip.exception.ErrorCode;
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
        return planRepository.findById(id).orElseThrow(()->new CustomException(ErrorCode.PLAN_NOT_FOUND));
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
    public void deleteById(int id) {
        Plan target = planRepository.findById(id).orElseThrow(()->new CustomException(ErrorCode.PLAN_NOT_FOUND));
        planRepository.delete(target);
    }

    @Transactional
    public Plan findByUserIdAndId(int userId, int planId){
        return planRepository.findByUserIdAndId(userId,planId);
    }
}