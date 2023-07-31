package com.example.memorip.repository;

import com.example.memorip.entity.TravelLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface TravelLikeRepository extends JpaRepository<TravelLike,Integer> {
    @Override
    ArrayList<TravelLike> findAll();

    @Override
    TravelLike save(TravelLike entity);


    @Query("SELECT l " +
            "FROM TravelLike l, Travel w " +
            "WHERE l.travel.id = w.id and w.isPublic = true and l.user.id = :userId and l.isLiked = 1")
    ArrayList<TravelLike> findByUserId(int userId);

    @Query("SELECT l FROM TravelLike l WHERE l.travel.id = :travelId and l.isLiked = 1")
    ArrayList<TravelLike> findByTravelId(int travelId);

    @Query("SELECT l FROM TravelLike l WHERE l.user.id = :userId AND l.travel.id = :travelId")
    TravelLike findLikeById(int userId, int travelId);

    void deleteByTravelId(int travelId);
}