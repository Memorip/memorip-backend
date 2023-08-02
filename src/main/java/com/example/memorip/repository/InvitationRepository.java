package com.example.memorip.repository;

import com.example.memorip.entity.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface InvitationRepository extends JpaRepository<Invitation, Integer> {
    @Query("SELECT i FROM Invitation i WHERE i.slug = :slug")
    Optional<Invitation> findOneBySlug(String slug);

    @Query("SELECT i FROM Invitation i WHERE i.plan.id = :planId")
    Optional<Invitation> findOneByPlanId(Integer planId);
}
