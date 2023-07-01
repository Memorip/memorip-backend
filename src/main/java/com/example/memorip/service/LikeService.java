package com.example.memorip.service;
import com.example.memorip.entity.Like;
import com.example.memorip.repository.LikeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@Service
public class LikeService {

    private final LikeRepository likeRepository;

    public LikeService(LikeRepository likeRepository){
        this.likeRepository=likeRepository;
    }

    @Transactional
    public ArrayList<Like> findAll(){
        return likeRepository.findAll();
    }

    @Transactional
    public Like save(Like entity){
        return likeRepository.save(entity);
    }

    @Transactional
    public Like findLikeById(int userId, int planId) {
        return likeRepository.findLikeById(userId, planId);
    }

    @Transactional
    public ArrayList<Like> findByuserId(int userId){
        return this.likeRepository.findByUserId(userId);
    }

    @Transactional
    public ArrayList<Like> findByplanId(int planId){
        return this.likeRepository.findByplanId(planId);
    }
}