package com.example.memorip.controller;

import com.example.memorip.dto.LikeDTO;
import com.example.memorip.dto.PlanDTO;
import com.example.memorip.entity.Like;
import com.example.memorip.entity.Plan;
import com.example.memorip.exception.DefaultRes;
import com.example.memorip.repository.LikeMapper;
import com.example.memorip.service.LikeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
            dtoList.add(likeMapper.likeToLikeDTO(like));
        }
        return new ResponseEntity<>(DefaultRes.res(200, "success", dtoList), HttpStatus.OK);
    }

    @PostMapping("/likes/{plan_id}/{user_id}")
    public ResponseEntity<?> saveLike(@PathVariable int plan_id,@PathVariable int user_id) {
        Like like = likeService.findLikeById(user_id,plan_id);
        if(like!=null){
            LikeDTO dto = likeMapper.likeToLikeDTO(like);
            if(dto.getIsLiked()==0){
                dto.setIsLiked(1);
            }else{
                String errorMessage = "이미 좋아요를 누른 여행일정이에요.";
                return new ResponseEntity<>(DefaultRes.res(400, errorMessage, ""), HttpStatus.BAD_REQUEST);
            }
            Like entity = likeMapper.LikeDTOtoLike(dto);
            Like savedLike = likeService.save(entity);
            return new ResponseEntity<>(DefaultRes.res(200, "success", savedLike), HttpStatus.OK);
        }
        else{
            LikeDTO newdto = new LikeDTO();
            newdto.setPlanId(plan_id);
            newdto.setUserId(user_id);
            newdto.setIsLiked(1);
            Like entity = likeMapper.LikeDTOtoLike(newdto);
            Like savedLike = likeService.save(entity);
            return new ResponseEntity<>(DefaultRes.res(200, "success", savedLike), HttpStatus.OK);

        }
    }

    @PostMapping ("/likes/cancel/{plan_id}/{user_id}")
    public ResponseEntity<?> cancelLike(@PathVariable int plan_id,@PathVariable int user_id) {
        Like like = likeService.findLikeById(user_id,plan_id);
        if(like==null){
            String errorMessage = "좋아요를 취소할 수 없어요.";
            return new ResponseEntity<>(DefaultRes.res(400, errorMessage, ""), HttpStatus.BAD_REQUEST);
        }

        LikeDTO dto = likeMapper.likeToLikeDTO(like);
        dto.setIsLiked(0);

        Like entity = likeMapper.LikeDTOtoLike(dto);
        Like savedLike = likeService.save(entity);
        return new ResponseEntity<>(DefaultRes.res(200, "success", savedLike), HttpStatus.OK);
    }

}
