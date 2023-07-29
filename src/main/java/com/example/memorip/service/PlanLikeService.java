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

    private final PlanLikeRepository likeRepository;

    public PlanLikeService(PlanLikeRepository likeRepository){
        this.likeRepository=likeRepository;
    }

    @Transactional
    public ArrayList<PlanLike> findAll(){
        return likeRepository.findAll();
    }

    @Transactional
    public PlanLike save(PlanLike entity){
        return likeRepository.save(entity);
    }

    @Transactional
    public PlanLike findLikeById(int userId, int planId) {
        return likeRepository.findLikeById(userId, planId);
    }

    @Transactional
    public ArrayList<PlanLike> findByuserId(int userId){
        return this.likeRepository.findByUserId(userId);
    }

    @Transactional
    public ArrayList<PlanLike> findByplanId(int planId){
        return this.likeRepository.findByplanId(planId);
    }
}