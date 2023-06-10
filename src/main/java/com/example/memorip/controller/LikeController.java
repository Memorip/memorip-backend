package com.example.memorip.controller;

import com.example.memorip.dto.LikeDTO;
import com.example.memorip.entity.Like;
import com.example.memorip.exception.DefaultRes;
import com.example.memorip.repository.LikeMapper;
import com.example.memorip.service.LikeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@Validated
public class LikeController {
    private final LikeService likeService;
    private final LikeMapper likeMapper;


    LikeController(LikeService likeService,LikeMapper likeMapper){
        this.likeService=likeService;
        this.likeMapper=likeMapper;
    }

    @GetMapping("/likes")
    public ResponseEntity<?> slectAll(){
        List<Like> lists = likeService.findAll();
        ArrayList<LikeDTO> dtoList = new ArrayList<>();
        if (lists.size() == 0) {
            String errorMessage = "좋아요 내역이 없어요.";
            return new ResponseEntity<>(DefaultRes.res(400, errorMessage, ""), HttpStatus.BAD_REQUEST);
        }
        for(Like like : lists){
            dtoList.add(LikeMapper.INSTANCE.likeToLikeDTO(like));
        }
        return new ResponseEntity<>(DefaultRes.res(200, "success", dtoList), HttpStatus.OK);
    }

}
