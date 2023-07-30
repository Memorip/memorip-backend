package com.example.memorip.service;
import com.example.memorip.entity.TravelLike;
import com.example.memorip.repository.TravelLikeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Slf4j
@Service
public class TravelLikeService {

    private final TravelLikeRepository travelLikeRepository;

    public TravelLikeService(TravelLikeRepository travelLikeRepository){
        this.travelLikeRepository=travelLikeRepository;
    }

    @Transactional
    public ArrayList<TravelLike> findAll(){
        return travelLikeRepository.findAll();
    }

    @Transactional
    public TravelLike save(TravelLike entity){
        return travelLikeRepository.save(entity);
    }

    @Transactional
    public TravelLike findLikeById(int userId, int travelId) {
        return travelLikeRepository.findLikeById(userId, travelId);
    }

    @Transactional
    public ArrayList<TravelLike> findByUserId(int userId){
        return travelLikeRepository.findByUserId(userId);
    }

    @Transactional
    public ArrayList<TravelLike> findByTravelId(int travelId){
        return travelLikeRepository.findByTravelId(travelId);
    }

    @Transactional
    public void deleteByTravelId(int travelId){
        ArrayList<TravelLike> travelLikes = travelLikeRepository.findByTravelId(travelId);
        travelLikeRepository.deleteAll(travelLikes);
    }

}