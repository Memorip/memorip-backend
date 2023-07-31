package com.example.memorip.controller;

import com.example.memorip.dto.*;
import com.example.memorip.entity.*;
import com.example.memorip.exception.DefaultRes;
import com.example.memorip.repository.TravelLikeMapper;
import com.example.memorip.repository.TravelMapper;
import com.example.memorip.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "travellike", description = "여행기 좋아요 관련 api 입니다.")
@Slf4j
@RestController
@RequestMapping("/api/travel/likes")
@Validated
public class TravelLikeController {
    private final TravelLikeService travelLikeService;
    private final UserService userService;
    private final TravelService travelService;
    private final TravelLikeMapper travelLikeMapper;
    private final TravelMapper travelMapper;

    private final PlanService planService;

    public TravelLikeController(TravelLikeService travelLikeService, UserService userService, TravelService travelService, TravelLikeMapper travelLikeMapper, TravelMapper travelMapper, PlanService planService){
        this.travelLikeService = travelLikeService;
        this.userService = userService;
        this.travelService = travelService;
        this.travelLikeMapper = travelLikeMapper;
        this.travelMapper = travelMapper;
        this.planService=planService;
    }

    // 유저별 좋아요 조회
    @Operation(summary = "유저별 좋아요 조회", description = "유저별 좋아요수를 조회하는 메서드입니다.")
    @GetMapping("/user/{userId}")
    public ResponseEntity<DefaultRes<List<TravelLikeDTO>>> getLikesByUser(@PathVariable int userId){
        ArrayList<TravelLike> lists = travelLikeService.findByUserId(userId);
        ArrayList<TravelLikeDTO> dtoList = new ArrayList<>();
        if(lists.size()>0){
            for(TravelLike like : lists){
                dtoList.add(travelLikeMapper.likeToLikeDTO(like));
            }
        }

        return new ResponseEntity<>(DefaultRes.res(200, "success", dtoList), HttpStatus.OK);
    }

    // 여행일정별 좋아요 조회
    @Operation(summary = "여행기별 좋아요 조회", description = "여행기별로 좋아요를 조회하는 메서드입니다.")
    @GetMapping("/{travelId}")
    public ResponseEntity<DefaultRes<List<TravelLikeDTO>>> getLikesByTravelId(@PathVariable int travelId){
        List<TravelLike> lists = travelLikeService.findByTravelId(travelId);
        ArrayList<TravelLikeDTO> dtoList = new ArrayList<>();
        if (lists.size()>0) {
            for(TravelLike like : lists){
                dtoList.add(travelLikeMapper.likeToLikeDTO(like));
            }
        }

        return new ResponseEntity<>(DefaultRes.res(200, "success", dtoList), HttpStatus.OK);
    }

    // 좋아요 추가
    @Operation(summary = "좋아요 추가", description = "여행기에 좋아요를 추가하는 메서드입니다.")
    @PostMapping("")
    public ResponseEntity<DefaultRes<TravelLikeDTO>> saveLike(@Valid @RequestBody TravelLikeRequest dto) {
        int userId = dto.getUserId();
        int travelId = dto.getTravelId();

        User user = userService.getUserById(userId);
        Travel travel = travelService.findById(travelId);
        TravelLike like = travelLikeService.findLikeById(userId,travelId);

        Plan plan = travel.getPlan();

         if(like!=null){
            // 좋아요 취소했다가 다시 추가했을 때
            TravelLikeDTO likeDto = travelLikeMapper.likeToLikeDTO(like);
            if(likeDto.getIsLiked()==0){
                likeDto.setIsLiked(1);
            }else{
                String errorMessage = "이미 좋아요를 누른 여행일정이에요.";
                return new ResponseEntity<>(DefaultRes.res(400, errorMessage, null), HttpStatus.BAD_REQUEST);
            }
            TravelLike entity = travelLikeMapper.LikeDTOtoLike(likeDto);

            entity.setUser(user);
            entity.setTravel(travel);

            TravelLike savedLike = travelLikeService.save(entity);

            // 좋아요 추가
            TravelDTO travelDto = travelMapper.travelToTravelDTO(travel);
            travelDto.setLikes(travelDto.getLikes()+1);
            Travel travelEntity = travelMapper.travelDTOtoTravel(travelDto);
            travelEntity.setUser(user);
            travelEntity.setPlan(plan);
            Travel savedTravel = travelService.save(travelEntity);

            TravelLikeDTO savedLikeDto = travelLikeMapper.likeToLikeDTO(savedLike);
            return new ResponseEntity<>(DefaultRes.res(200, "success", savedLikeDto), HttpStatus.OK);
        }
        else{
            // 처음 좋아요를 눌렀을때...
            TravelLikeDTO newdto = new TravelLikeDTO();
            newdto.setTravelId(travelId);
            newdto.setUserId(userId);
            newdto.setIsLiked(1);

            // 좋아요 추가
            TravelDTO travelDTO = travelMapper.travelToTravelDTO(travel);
            travelDTO.setLikes(travelDTO.getLikes()+1);

            Travel travelEntity = travelMapper.travelDTOtoTravel(travelDTO);

            travelEntity.setUser(user);
            travelEntity.setPlan(plan);

            Travel savedTravel = travelService.save(travelEntity);

            TravelLike entity = travelLikeMapper.LikeDTOtoLike(newdto);

            entity.setUser(user);
            entity.setTravel(travel);

            TravelLike savedLike = travelLikeService.save(entity);

            TravelLikeDTO savedLikeDto = travelLikeMapper.likeToLikeDTO(savedLike);
            return new ResponseEntity<>(DefaultRes.res(200, "success", savedLikeDto), HttpStatus.OK);
        }
    }

    // 좋아요 취소
    @Operation(summary = "좋아요 취소", description = "여행기에 좋아요를 취소하는 메서드입니다.")
    @PatchMapping ("/cancel")
    public ResponseEntity<DefaultRes<TravelLikeDTO>> cancelLike(@Valid @RequestBody TravelLikeRequest dto) {
        int userId = dto.getUserId();
        int travelId = dto.getTravelId();

        User user = userService.getUserById(userId);
        TravelLike like = travelLikeService.findLikeById(userId,travelId);
        Travel travel = travelService.findById(travelId);
        Plan plan = travel.getPlan();

        if(like==null){ // 좋아요를 추가한 기록이 없을 때
            String errorMessage = "좋아요를 취소할 수 없어요.";
            return new ResponseEntity<>(DefaultRes.res(400, errorMessage, null), HttpStatus.BAD_REQUEST);
        }else{
            TravelLikeDTO likeDto = travelLikeMapper.likeToLikeDTO(like);
            likeDto.setTravelId(travelId);
            likeDto.setUserId(userId);

            if(likeDto.getIsLiked()==1){ // 좋아요 취소
                likeDto.setIsLiked(0);
            }else{
                String errorMessage = "이미 좋아요를 취소한 여행일정이에요.";
                return new ResponseEntity<>(DefaultRes.res(400, errorMessage, null), HttpStatus.BAD_REQUEST);
            }
            TravelLike entity = travelLikeMapper.LikeDTOtoLike(likeDto);

            entity.setUser(user);
            entity.setTravel(travel);

            TravelLike savedLike = travelLikeService.save(entity);

            // 좋아요 취소
            TravelDTO travelDTO = travelMapper.travelToTravelDTO(travel);
            travelDTO.setLikes(travelDTO.getLikes()-1);

            Travel travelEntity = travelMapper.travelDTOtoTravel(travelDTO);
            travelEntity.setUser(user);
            travelEntity.setPlan(plan);
            Travel savedTravel = travelService.save(travelEntity);

            TravelLikeDTO savedLikeDto = travelLikeMapper.likeToLikeDTO(savedLike);
            return new ResponseEntity<>(DefaultRes.res(200, "success", savedLikeDto), HttpStatus.OK);
        }
    }

}