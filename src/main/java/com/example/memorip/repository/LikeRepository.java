package com.example.memorip.repository;

import com.example.memorip.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface LikeRepository extends JpaRepository<Like,Long> {
    @Override
    ArrayList<Like> findAll();

    @Override
    Like save(Like entity);

    Like findById(int id);

    @Query("SELECT l FROM Like l WHERE l.user_id = :userId AND l.plan_id = :planId")
    Like findLikeById(int userId, int planId);
}