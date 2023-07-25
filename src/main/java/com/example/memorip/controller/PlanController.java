package com.example.memorip.controller;

import com.example.memorip.dto.PlanDTO;
import com.example.memorip.entity.Plan;
import com.example.memorip.entity.User;
import com.example.memorip.exception.DefaultRes;
import com.example.memorip.repository.PlanMapper;
import com.example.memorip.service.PlanService;
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

@Tag(name = "plan", description = "여행일정 관련 api 입니다.")
@Slf4j
@RestController
@RequestMapping("/api/plans")
@Validated
public class PlanController {
    private final PlanService planService;

    private final UserService userService;
    private final PlanMapper planMapper;
    PlanController(PlanService planService, UserService userService, PlanMapper planMapper){
        this.planService=planService;
        this.userService = userService;
        this.planMapper=planMapper;
    }

    @Operation(summary = "여행일정 전체 조회", description = "모든 여행일정을 조회하는 메서드입니다.")
    @GetMapping("/")
    public ResponseEntity<DefaultRes<List<PlanDTO>>> getPlans(){
        List<Plan> lists = planService.findAll();
        ArrayList<PlanDTO> dtoList = new ArrayList<>();
        for(Plan plan : lists){
            dtoList.add(planMapper.planToPlanDTO(plan));
        }
        return new ResponseEntity<>(DefaultRes.res(200, "success", dtoList), HttpStatus.OK);
    }

    // 조회수 순 정렬
    @Operation(summary = "여행일정 조회순 정렬", description = "여행일정을 조회순으로 정렬하여 조회하는 메서드입니다.")
    @GetMapping("/view/sort")
    public ResponseEntity<DefaultRes<List<PlanDTO>>> sortPlanByViews(){
        List<Plan> lists = planService.sortByViews();
        ArrayList<PlanDTO> dtoList = new ArrayList<>();
        if (lists.size() == 0) {
            String errorMessage = "조회되는 여행 계획이 없어요.";
            return new ResponseEntity<>(DefaultRes.res(400, errorMessage, null), HttpStatus.BAD_REQUEST);
        }
        for(Plan plan : lists){
            dtoList.add(planMapper.planToPlanDTO(plan));
        }
        return new ResponseEntity<>(DefaultRes.res(200, "success", dtoList), HttpStatus.OK);
    }

    // 좋아요 순 정렬
    @Operation(summary = "여행일정 좋아요순 정렬", description = "여행일정을 좋아요순으로 정렬하여 조회하는 메서드입니다.")
    @GetMapping("/like/sort")
    public ResponseEntity<DefaultRes<List<PlanDTO>>> sortPlanByLikes(){
        List<Plan> lists = planService.sortByLikes();
        ArrayList<PlanDTO> dtoList = new ArrayList<>();
        if (lists.size() == 0) {
            String errorMessage = "조회되는 여행 계획이 없어요.";
            return new ResponseEntity<>(DefaultRes.res(400, errorMessage, null), HttpStatus.BAD_REQUEST);
        }
        for(Plan plan : lists){
            dtoList.add(planMapper.planToPlanDTO(plan));
        }
        return new ResponseEntity<>(DefaultRes.res(200, "success", dtoList), HttpStatus.OK);
    }

    @Operation(summary = "여행일정 상세조회", description = "여행일정 상세를 조회하는 메서드입니다.")
    @GetMapping("/{id}")
    public ResponseEntity<DefaultRes<PlanDTO>> getPlanById(@PathVariable int id){
        Plan     plan = planService.findById(id);
        if (plan == null) {
            String errorMessage = "조회되는 여행 계획이 없어요.";
            return new ResponseEntity<>(DefaultRes.res(400, errorMessage, null), HttpStatus.BAD_REQUEST);
        }
        PlanDTO dto = planMapper.planToPlanDTO(plan);
        log.info("dto:"+dto.getUserId());

        dto.setViews(dto.getViews()+1);
        Plan entity = planMapper.planDTOtoPlan(dto);
        int userId = dto.getUserId();
        User user = userService.getUserById(userId);
        entity.setUser(user);

        Plan savedPlan = planService.save(entity);
        return new ResponseEntity<>(DefaultRes.res(200, "success", dto), HttpStatus.OK);

    }

    @Operation(summary = "여행일정 추가", description = "여행일정을 추가하는 메서드입니다.")
    @PostMapping("/add")
    public ResponseEntity<DefaultRes<Plan>> savePlan(@Valid @RequestBody PlanDTO dto) {
        dto.setViews(0);

        int userId = dto.getUserId();
        User user = userService.getUserById(userId);

        if(user==null){
            String errorMessage = "일치하는 사용자를 찾을 수 없어요.";
            return new ResponseEntity<>(DefaultRes.res(400, errorMessage, null), HttpStatus.BAD_REQUEST);
        }

        //1. DTO -> 엔티티 변환
        Plan entity = planMapper.planDTOtoPlan(dto);
        entity.setUser(user);
        entity.setCreatedAt(LocalDateTime.now());
        //2. 엔티티 -> DB 저장
        Plan savedPlan = planService.save(entity);
        return new ResponseEntity<>(DefaultRes.res(200, "success", savedPlan), HttpStatus.OK);
    }

    @Operation(summary = "여행일정 수정", description = "여행일정을 수정하는 메서드입니다.")
    @PatchMapping("/add/{id}")
    public ResponseEntity<DefaultRes<Plan>> updatePlan(@Valid @PathVariable int id, @RequestBody PlanDTO dto){
        Plan plan = planService.findById(id);
        if(plan==null){
            String errorMessage = "수정할 여행 계획이 없어요.";
            return new ResponseEntity<>(DefaultRes.res(400, errorMessage, null), HttpStatus.BAD_REQUEST);
        }
        if(dto.getCity()!=null) {
            String cities = planMapper.cityListToString(dto.getCity());
            plan.setCity(cities);
        }
        if(dto.getStartDate()!=null) plan.setStartDate(dto.getStartDate());
        if(dto.getEndDate()!=null) plan.setEndDate(dto.getEndDate());
        if(dto.getTripType()!=null) plan.setTripType(dto.getTripType());
        if(dto.getParticipants()!=null) {
            String participants = planMapper.participantsIntegerToString(dto.getParticipants());
            plan.setParticipants(participants);
        }
        if(dto.getCreatedAt()!=null) plan.setCreatedAt(dto.getCreatedAt());
        if(dto.getIsPublic()!=null) plan.setIsPublic(true);
        Plan savedPlan = planService.save(plan);
        return new ResponseEntity<>(DefaultRes.res(200, "success",savedPlan), HttpStatus.OK);
    }

    @Operation(summary = "여행일정 삭제", description = "여행일정을 삭제하는 메서드입니다.")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<DefaultRes> deletePlanById(@PathVariable int id) {
        Plan removedPlan = planService.deleteById(id);
        if(removedPlan == null) {
            String errorMessage = "삭제할 여행 계획이 없어요.";
            return new ResponseEntity<>(DefaultRes.res(400, errorMessage, null), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(DefaultRes.res(200, "success", null), HttpStatus.OK);
    }
}