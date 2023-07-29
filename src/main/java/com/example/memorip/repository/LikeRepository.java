package com.example.memorip.repository;

import com.example.memorip.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface LikeRepository extends JpaRepository<Like,Integer> {
    @Override
    ArrayList<Like> findAll();

    @Override
    Like save(Like entity);

    @Query("SELECT l FROM Like l WHERE l.user.id = :userId and l.isLiked = 1")
    ArrayList<Like> findByUserId(int userId);

    @Query("SELECT l FROM Like l WHERE l.plan.id = :planId and l.isLiked = 1")
    ArrayList<Like> findByplanId(int planId);

    @Query("SELECT l FROM Like l WHERE l.user.id = :userId AND l.plan.id = :planId")
    Like findLikeById(int userId, int planId);


}