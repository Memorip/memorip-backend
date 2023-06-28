package com.example.memorip.controller;

import com.example.memorip.dto.LikeDTO;
import com.example.memorip.dto.PlanDTO;
import com.example.memorip.dto.PlanLikeDTO;
import com.example.memorip.entity.Like;
import com.example.memorip.entity.Plan;
import com.example.memorip.entity.User;
import com.example.memorip.exception.DefaultRes;
import com.example.memorip.repository.LikeMapper;
import com.example.memorip.repository.PlanMapper;
import com.example.memorip.service.LikeService;
import com.example.memorip.service.PlanService;
import com.example.memorip.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
    private final UserService userService;
    private final PlanService planService;
    private final LikeMapper likeMapper;
    private final PlanMapper planMapper;


    LikeController(LikeService likeService, UserService userService, PlanService planService, LikeMapper likeMapper, PlanMapper planMapper){
        this.likeService=likeService;
        this.userService = userService;
        this.planService = planService;
        this.likeMapper=likeMapper;
        this.planMapper = planMapper;
    }

    // 유저별 좋아요 조회
    @GetMapping("/likes/user/{userId}")
    public ResponseEntity<?> getLikesByUser(@PathVariable int userId){
        List<Like> lists = likeService.findByuserId(userId);
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
    @GetMapping("/likes/plan/{planId}")
    public ResponseEntity<?> getLikesByPlan(@PathVariable int planId){


        List<Like> lists = likeService.findByplanId(planId);
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
    @PostMapping("/likes")
    public ResponseEntity<?> saveLike(@Valid @RequestBody PlanLikeDTO dto) {

        int userId = dto.getUserId();

        log.info("userId:"+dto.getUserId());

        int planId = dto.getPlanId();
        log.info("planId:"+dto.getPlanId());
        User user = userService.getUserById(userId);
        log.info("user:"+user.toString());
        if(user==null){
            String errorMessage = "일치하는 사용자가 없어요.";
            return new ResponseEntity<>(DefaultRes.res(400, errorMessage, ""), HttpStatus.BAD_REQUEST);
        }

        Plan plan = planService.findById(planId);
        log.info("plan:"+plan.toString());

        if (plan==null){
            String errorMessage = "조회되는 여행 계획이 없어요.";
            return new ResponseEntity<>(DefaultRes.res(400, errorMessage, ""), HttpStatus.BAD_REQUEST);
        }

        Like like = likeService.findLikeById(userId,planId);


        if(like!=null){
            // 좋아요 취소했다가 다시 추가했을 때
            LikeDTO likeDto = likeMapper.likeToLikeDTO(like);
            if(likeDto.getIsLiked()==0){
                likeDto.setIsLiked(1);
            }else{
                String errorMessage = "이미 좋아요를 누른 여행일정이에요.";
                return new ResponseEntity<>(DefaultRes.res(400, errorMessage, ""), HttpStatus.BAD_REQUEST);
            }
            Like entity = likeMapper.LikeDTOtoLike(likeDto);

            entity.setUser(user);
            entity.setPlan(plan);

            Like savedLike = likeService.save(entity);

            // 좋아요 추가
            PlanDTO plandto = planMapper.planToPlanDTO(plan);
            plandto.setLikes(plandto.getLikes()+1);
            Plan planentity = planMapper.planDTOtoPlan(plandto);
            planentity.setUser(user);
            Plan savedPlan = planService.save(planentity);


            return new ResponseEntity<>(DefaultRes.res(200, "success", savedLike), HttpStatus.OK);
        }
        else{
            // 처음 좋아요를 눌렀을때...
            LikeDTO newdto = new LikeDTO();
            newdto.setPlanId(planId);
            newdto.setUserId(userId);
            newdto.setIsLiked(1);

            // 좋아요 추가
            PlanDTO plandto = planMapper.planToPlanDTO(plan);
            plandto.setLikes(plandto.getLikes()+1);
            Plan planentity = planMapper.planDTOtoPlan(plandto);

            planentity.setUser(user);
            Plan savedPlan = planService.save(planentity);

            Like entity = likeMapper.LikeDTOtoLike(newdto);

            entity.setUser(user);
            entity.setPlan(plan);

            Like savedLike = likeService.save(entity);
            return new ResponseEntity<>(DefaultRes.res(200, "success", savedLike), HttpStatus.OK);
        }
    }

    // 좋아요 취소
    @PatchMapping ("/likes/cancel")
    public ResponseEntity<?> cancelLike(@Valid @RequestBody PlanLikeDTO dto) {
        int userId = dto.getUserId();
        int planId = dto.getPlanId();

        User user = userService.getUserById(userId);
        Like like = likeService.findLikeById(userId,planId);
        Plan plan = planService.findById(planId);

        if(user==null){
            String errorMessage = "일치하는 사용자가 없어요.";
            return new ResponseEntity<>(DefaultRes.res(400, errorMessage, ""), HttpStatus.BAD_REQUEST);
        }

        if (plan==null){
            String errorMessage = "조회되는 여행 계획이 없어요.";
            return new ResponseEntity<>(DefaultRes.res(400, errorMessage, ""), HttpStatus.BAD_REQUEST);
        }


        if(like==null){ // 좋아요를 추가한 기록이 없을 때
            String errorMessage = "좋아요를 취소할 수 없어요.";
            return new ResponseEntity<>(DefaultRes.res(400, errorMessage, ""), HttpStatus.BAD_REQUEST);
        }else{
            LikeDTO likeDto = likeMapper.likeToLikeDTO(like);
            if(likeDto.getIsLiked()==1){ // 좋아요 취소
                likeDto.setIsLiked(0);
            }else{
                String errorMessage = "이미 좋아요를 취소한 여행일정이에요.";
                return new ResponseEntity<>(DefaultRes.res(400, errorMessage, ""), HttpStatus.BAD_REQUEST);
            }
            Like entity = likeMapper.LikeDTOtoLike(likeDto);

            entity.setUser(user);
            entity.setPlan(plan);

            Like savedLike = likeService.save(entity);

            // 좋아요 취소
            PlanDTO plandto = planMapper.planToPlanDTO(plan);
            plandto.setLikes(plandto.getLikes()-1);
            Plan planentity = planMapper.planDTOtoPlan(plandto);
            // planentity.setUser(user);
            Plan savedPlan = planService.save(planentity);

            return new ResponseEntity<>(DefaultRes.res(200, "success", savedLike), HttpStatus.OK);
        }
    }

}