package com.example.memorip.repository;

import com.example.memorip.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface LikeRepository extends JpaRepository<Like,Long> {
    @Override
    ArrayList<Like> findAll();
}
