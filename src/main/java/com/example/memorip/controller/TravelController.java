package com.example.memorip.controller;

import com.example.memorip.dto.PlanDTO;
import com.example.memorip.dto.TravelDTO;
import com.example.memorip.entity.Plan;
import com.example.memorip.entity.Travel;
import com.example.memorip.entity.User;
import com.example.memorip.exception.CustomException;
import com.example.memorip.exception.DefaultRes;
import com.example.memorip.exception.ErrorCode;
import com.example.memorip.repository.TravelMapper;
import com.example.memorip.service.PlanService;
import com.example.memorip.service.TravelService;
import com.example.memorip.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "travle", description = "여행기 관련 api 입니다.")
@Slf4j
@RestController
@RequestMapping("/api/travel")
@Validated
public class TravelController {
    private final TravelService travelService;
    private final UserService userService;

    private final PlanService planService;
    private final TravelMapper travelMapper;

    public TravelController(TravelService travelService, UserService userService, PlanService planService, TravelMapper travelMapper) {
        this.travelService = travelService;
        this.userService = userService;
        this.planService = planService;
        this.travelMapper = travelMapper;
    }

    @Operation(summary = "여행기 전체 조회", description = "전체 여행기를 조회하는 메서드입니다.")
    @GetMapping("")
    public ResponseEntity<?> getTravels(){
        ArrayList<Travel> list = travelService.findAllTravels();
        ArrayList<TravelDTO> dtoList = new ArrayList<>();
        for(Travel travel : list){
            dtoList.add(travelMapper.travelToTravelDTO(travel));
        }
        return new ResponseEntity<>(DefaultRes.res(200, "success", dtoList), HttpStatus.OK);
    }

    @Operation(summary = "여행기 상세 조회", description = "상세 여행기를 조회하는 메서드입니다.")
    @GetMapping("/{id}")
    public ResponseEntity<?> getTravelById(@PathVariable int id){
        Travel travel = travelService.findById(id);
        if(travel==null){
            String errorMessage = "조회되는 여행기가 없어요.";
            return new ResponseEntity<>(DefaultRes.res(400, errorMessage, ""), HttpStatus.BAD_REQUEST);
        }

        TravelDTO dto = travelMapper.travelToTravelDTO(travel);
        return new ResponseEntity<>(DefaultRes.res(200, "success", dto), HttpStatus.OK);
    }

    @Operation(summary = "유저별 여행기 조회", description = "유저별로 작성한 여행기를 조회하는 메서드입니다.")
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getTravelByUserId(@PathVariable int userId){
        User user = userService.getUserById(userId);
        if(user==null){
            String errorMessage = "일치하는 사용자가 없어요.";
            return new ResponseEntity<>(DefaultRes.res(400, errorMessage, ""), HttpStatus.BAD_REQUEST);
        }
        ArrayList<Travel> lists = travelService.findByUserId(userId);
        ArrayList<TravelDTO> dtoList = new ArrayList<>();
        for(Travel travel : lists){
            dtoList.add(travelMapper.travelToTravelDTO(travel));
        }
        return new ResponseEntity<>(DefaultRes.res(200, "success", dtoList), HttpStatus.OK);
    }


    @Operation(summary = "여행기 등록", description = "여행기를 등록하는 메서드입니다.")
    @PostMapping("/add")
    public ResponseEntity<?> saveTravel(@Valid @RequestBody TravelDTO dto) {
        dto.setViews(0);
        dto.setLikes(0);
        dto.setCreatedAt(LocalDateTime.now());

        int userId = dto.getUserId();
        User user = userService.getUserById(userId);

        if(user==null){
            String errorMessage = "일치하는 사용자를 찾을 수 없어요.";
            return new ResponseEntity<>(DefaultRes.res(400, errorMessage, ""), HttpStatus.BAD_REQUEST);
        }


        int planId = dto.getPlanId();
        Plan plan = planService.findByUserIdAndId(userId,planId);
        if(plan==null){ // 사용자의 여행계획이 아닐 때
            String errorMessage = "여행기를 작성할 수 있는 여행계획이 아니에요.";
            return new ResponseEntity<>(DefaultRes.res(400, errorMessage, ""), HttpStatus.BAD_REQUEST);
        }

        //1. DTO -> 엔티티 변환
        Travel entity = travelMapper.travelDTOtoTravel(dto);
        entity.setUser(user);
        entity.setPlan(plan);

        //2. 엔티티 -> DB 저장
        Travel savedTravel = travelService.save(entity);

        //2. 엔티티 -> DTO 반환
        TravelDTO savedDto = travelMapper.travelToTravelDTO(savedTravel);
        return new ResponseEntity<>(DefaultRes.res(201, "success", savedDto), HttpStatus.OK);
    }


    @Operation(summary = "여행기 수정", description = "여행기를 수정하는 메서드입니다.")
    @PatchMapping("/add/{id}")
    public ResponseEntity<?> updateTravel(@Valid @PathVariable int id, @RequestBody TravelDTO dto){
        Travel travel = travelService.findById(id);
        if(travel==null){
            String errorMessage = "수정할 여행기가 없어요.";
            return new ResponseEntity<>(DefaultRes.res(400, errorMessage, ""), HttpStatus.BAD_REQUEST);
        }

        int userId = travel.getUser().getId();
        int planId = travel.getPlan().getId();
        Plan plan = planService.findByUserIdAndId(userId,planId);
        if(plan==null){ // 사용자의 여행계획이 아닐 때
            String errorMessage = "여행기를 작성할 수 있는 여행계획이 아니에요.";
            return new ResponseEntity<>(DefaultRes.res(400, errorMessage, ""), HttpStatus.BAD_REQUEST);
        }

        if(dto.getTitle()!=null) travel.setTitle(dto.getTitle());
        if(dto.getContent()!=null) travel.setContent(dto.getContent());
        if(dto.getIsPublic()!=null) travel.setIsPublic(dto.getIsPublic());

        Travel savedTravel = travelService.save(travel);
        TravelDTO savedTravelDTO = travelMapper.travelToTravelDTO(savedTravel);
        return new ResponseEntity<>(DefaultRes.res(200, "success",savedTravelDTO), HttpStatus.OK);
    }

    @Operation(summary = "여행기 삭제", description = "여행기를 삭제하는 메서드입니다.")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTravelById(@PathVariable int id) {
        Travel deletedTravel = travelService.deleteById(id);
        if(deletedTravel==null){
            String errorMessage = "삭제할 여행기가 없어요.";
            return new ResponseEntity<>(DefaultRes.res(400, errorMessage, ""), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(DefaultRes.res(200, "success", null), HttpStatus.OK);
    }

}
