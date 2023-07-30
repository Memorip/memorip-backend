package com.example.memorip.service;
import com.example.memorip.entity.PlanLike;
import com.example.memorip.repository.PlanLikeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Slf4j
@Service
public class PlanLikeService {

    private final PlanLikeRepository planLikeRepository;

    public PlanLikeService(PlanLikeRepository planLikeRepository){
        this.planLikeRepository=planLikeRepository;
    }

    @Transactional
    public ArrayList<PlanLike> findAll(){
        return planLikeRepository.findAll();
    }

    @Transactional
    public PlanLike save(PlanLike entity){
        return planLikeRepository.save(entity);
    }

    @Transactional
    public PlanLike findLikeById(int userId, int planId) {
        return planLikeRepository.findLikeById(userId, planId);
    }

    @Transactional
    public ArrayList<PlanLike> findByuserId(int userId){
        return planLikeRepository.findByUserId(userId);
    }

    @Transactional
    public ArrayList<PlanLike> findByplanId(int planId){
        return planLikeRepository.findByplanId(planId);
    }

    @Transactional
    public void deleteByPlanId(int planId){
        ArrayList<PlanLike> planLikes = planLikeRepository.findByplanId(planId);
        planLikeRepository.deleteAll(planLikes);
    }

}