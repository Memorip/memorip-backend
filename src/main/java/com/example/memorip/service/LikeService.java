package com.example.memorip.service;

import com.example.memorip.entity.Like;
import com.example.memorip.repository.LikeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private LikeService(LikeRepository likeRepository){
        this.likeRepository=likeRepository;
    }

    public ArrayList<Like> findAll(){
        return likeRepository.findAll();
    }

    public Like save(Like entity){
        return likeRepository.save(entity);
    }


    public Like findLikeById(int userId, int planId) {
        return likeRepository.findLikeById(userId, planId);
    }

    public ArrayList<Like> findByuserId(int userId){
        return this.likeRepository.findByUserId(userId);
    }

    public ArrayList<Like> findByplanId(int planId){
        return this.likeRepository.findByplanId(planId);
    }
}
