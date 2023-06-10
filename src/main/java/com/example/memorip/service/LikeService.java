package com.example.memorip.service;

import com.example.memorip.entity.Like;
import com.example.memorip.repository.LikeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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

}
