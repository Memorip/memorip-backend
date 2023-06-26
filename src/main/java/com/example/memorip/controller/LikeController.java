package com.example.memorip.controller;

import com.example.memorip.dto.LikeDTO;
import com.example.memorip.dto.PlanDTO;
import com.example.memorip.entity.Like;
import com.example.memorip.entity.Plan;
import com.example.memorip.exception.DefaultRes;
import com.example.memorip.repository.LikeMapper;
import com.example.memorip.repository.PlanMapper;
import com.example.memorip.service.LikeService;
import com.example.memorip.service.PlanService;
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
    private final PlanService planService;
    private final LikeMapper likeMapper;
    private final PlanMapper planMapper;


    LikeController(LikeService likeService, PlanService planService, LikeMapper likeMapper, PlanMapper planMapper){
        this.likeService=likeService;
        this.planService = planService;
        this.likeMapper=likeMapper;
        this.planMapper = planMapper;
    }

    // 좋아요 전체 조회
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

    // 유저별 좋아요 조회
    @GetMapping("/likes/user/{user_id}")
    public ResponseEntity<?> slectByUser(@PathVariable int user_id){
        List<Like> lists = likeService.findByuserId(user_id);
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

    // 여행일정별 좋아요 조회
    @GetMapping("/likes/plan/{plan_id}")
    public ResponseEntity<?> slectByPlan(@PathVariable int plan_id){
        List<Like> lists = likeService.findByplanId(plan_id);
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

    // 좋아요 추가
    @PostMapping("/likes/{plan_id}/{user_id}")
    public ResponseEntity<?> saveLike(@PathVariable int plan_id,@PathVariable int user_id) {
        Like like = likeService.findLikeById(user_id,plan_id);
        Plan plan = planService.findById(plan_id);

        if (plan==null){
            String errorMessage = "조회되는 여행 계획이 없어요.";
            return new ResponseEntity<>(DefaultRes.res(400, errorMessage, ""), HttpStatus.BAD_REQUEST);
        }

        if(like!=null){
            // 좋아요 취소했다가 다시 추가했을 때
            LikeDTO dto = likeMapper.likeToLikeDTO(like);
            if(dto.getIsLiked()==0){
                dto.setIsLiked(1);
            }else{
                String errorMessage = "이미 좋아요를 누른 여행일정이에요.";
                return new ResponseEntity<>(DefaultRes.res(400, errorMessage, ""), HttpStatus.BAD_REQUEST);
            }
            Like entity = likeMapper.LikeDTOtoLike(dto);
            Like savedLike = likeService.save(entity);

            // 좋아요 추가
            PlanDTO plandto = planMapper.planToPlanDTO(plan);
            plandto.setLikes(plandto.getLikes()+1);
            Plan planentity = planMapper.planDTOtoPlan(plandto);
            Plan savedPlan = planService.save(planentity);

            return new ResponseEntity<>(DefaultRes.res(200, "success", savedLike), HttpStatus.OK);
        }
        else{
            // 처음 좋아요를 눌렀을때...
            LikeDTO newdto = new LikeDTO();
            newdto.setPlanId(plan_id);
            newdto.setUserId(user_id);
            newdto.setIsLiked(1);

            // 좋아요 추가
            PlanDTO plandto = planMapper.planToPlanDTO(plan);
            plandto.setLikes(plandto.getLikes()+1);
            Plan planentity = planMapper.planDTOtoPlan(plandto);
            Plan savedPlan = planService.save(planentity);

            Like entity = likeMapper.LikeDTOtoLike(newdto);
            Like savedLike = likeService.save(entity);
            return new ResponseEntity<>(DefaultRes.res(200, "success", savedLike), HttpStatus.OK);
        }
    }

    // 좋아요 취소
    @PostMapping ("/likes/cancel/{plan_id}/{user_id}")
    public ResponseEntity<?> cancelLike(@PathVariable int plan_id,@PathVariable int user_id) {
        Like like = likeService.findLikeById(user_id,plan_id);
        Plan plan = planService.findById(plan_id);

        if (plan==null){
            String errorMessage = "조회되는 여행 계획이 없어요.";
            return new ResponseEntity<>(DefaultRes.res(400, errorMessage, ""), HttpStatus.BAD_REQUEST);
        }


        if(like==null){ // 좋아요를 추가한 기록이 없을 때
            String errorMessage = "좋아요를 취소할 수 없어요.";
            return new ResponseEntity<>(DefaultRes.res(400, errorMessage, ""), HttpStatus.BAD_REQUEST);
        }else{
            LikeDTO dto = likeMapper.likeToLikeDTO(like);
            if(dto.getIsLiked()==1){ // 좋아요 취소
                dto.setIsLiked(0);
            }else{
                String errorMessage = "이미 좋아요를 취소한 여행일정이에요.";
                return new ResponseEntity<>(DefaultRes.res(400, errorMessage, ""), HttpStatus.BAD_REQUEST);
            }
            Like entity = likeMapper.LikeDTOtoLike(dto);
            Like savedLike = likeService.save(entity);

            // 좋아요 취소
            PlanDTO plandto = planMapper.planToPlanDTO(plan);
            plandto.setLikes(plandto.getLikes()-1);
            Plan planentity = planMapper.planDTOtoPlan(plandto);
            Plan savedPlan = planService.save(planentity);

            return new ResponseEntity<>(DefaultRes.res(200, "success", savedLike), HttpStatus.OK);
        }
    }

}