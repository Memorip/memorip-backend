package com.example.memorip.repository;

import com.example.memorip.entity.PlanLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface PlanLikeRepository extends JpaRepository<PlanLike,Integer> {
    @Override
    ArrayList<PlanLike> findAll();

    @Override
    PlanLike save(PlanLike entity);

    @Query("SELECT l FROM PlanLike l WHERE l.user.id = :userId and l.isLiked = 1")
    ArrayList<PlanLike> findByUserId(int userId);

    @Query("SELECT l FROM PlanLike l WHERE l.plan.id = :planId and l.isLiked = 1")
    ArrayList<PlanLike> findByplanId(int planId);

    @Query("SELECT l FROM PlanLike l WHERE l.user.id = :userId AND l.plan.id = :planId")
    PlanLike findLikeById(int userId, int planId);

    void deleteByPlanId(int planId);
}