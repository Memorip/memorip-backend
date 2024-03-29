package com.example.memorip.controller;

import com.example.memorip.dto.plan.PlanDTO;
import com.example.memorip.dto.plan.PlanRequest;
import com.example.memorip.dto.plan.PlanResponse;
import com.example.memorip.entity.Plan;
import com.example.memorip.entity.User;
import com.example.memorip.exception.CustomException;
import com.example.memorip.exception.DefaultRes;
import com.example.memorip.exception.ErrorCode;
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
import java.util.List;
import java.util.stream.Collectors;

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

    @Operation(summary = "여행일정 전체 조회", description = "여행일정을 전체 조회하는 메서드입니다.")
    @GetMapping("")
    public ResponseEntity<DefaultRes<List<PlanResponse>>> getPlans(){
        List<Plan> plans = planService.findAll();
        List<PlanResponse> dtoList = plans.stream()
                .map(plan -> planMapper.planDTOtoPlanResponse(planMapper.planToPlanDTO(plan),plan.getUser().getNickname()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(DefaultRes.res(200, "success", dtoList));
    }

    // 조회수 순 정렬
    @Operation(summary = "여행일정 조회순 정렬", description = "여행일정을 조회순으로 정렬하여 조회하는 메서드입니다.")
    @GetMapping("/view/sort")
    public ResponseEntity<DefaultRes<List<PlanResponse>>> sortPlanByViews(){
        List<Plan> plans = planService.sortByViews();
        List<PlanResponse> dtoList = plans.stream()
                .map(plan -> planMapper.planDTOtoPlanResponse(planMapper.planToPlanDTO(plan),plan.getUser().getNickname()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(DefaultRes.res(200, "success", dtoList));
    }

    // 좋아요 순 정렬
    @Operation(summary = "여행일정 좋아요순 정렬", description = "여행일정을 좋아요순으로 정렬하여 조회하는 메서드입니다.")
    @GetMapping("/like/sort")
    public ResponseEntity<DefaultRes<List<PlanResponse>>> sortPlanByLikes(){
        List<Plan> plans = planService.sortByLikes();
        if (plans.isEmpty()) {
            String errorMessage = "조회되는 여행 계획이 없어요.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(DefaultRes.res(400, errorMessage, null));
        }
        List<PlanResponse> dtoList = plans.stream()
                .map(plan -> planMapper.planDTOtoPlanResponse(planMapper.planToPlanDTO(plan),plan.getUser().getNickname()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(DefaultRes.res(200, "success", dtoList));
    }

    @Operation(summary = "여행일정 상세조회", description = "여행일정 상세를 조회하는 메서드입니다.")
    @GetMapping("/{id}")
    public ResponseEntity<DefaultRes<PlanResponse>> getPlanById(@PathVariable int id){
        Plan plan = planService.findById(id);

        if(!plan.getIsPublic()){
            throw new CustomException(ErrorCode.ACCESS_DENIED_PLAN);
        }

        PlanDTO dto = planMapper.planToPlanDTO(plan);
        dto.setViews(dto.getViews()+1);
        Plan entity = planMapper.planDTOtoPlan(dto);
        int userId = dto.getUserId();
        User user = userService.getUserById(userId);
        entity.setUser(user);
        Plan savedPlan = planService.save(entity);
        PlanDTO savedDto = planMapper.planToPlanDTO(savedPlan);
        PlanResponse resDto = planMapper.planDTOtoPlanResponse(savedDto,plan.getUser().getNickname());
        return ResponseEntity.ok(DefaultRes.res(200, "success", resDto));
    }

    @Operation(summary = "유저별 여행일정 조회", description = "유저별로 여행일정을 조회하는 메서드입니다.")
    @GetMapping("/user/{userId}")
    public ResponseEntity<DefaultRes<List<PlanResponse>>> getPlanByUserId(@PathVariable int userId){
        List<Plan> plans = planService.findByUserId(userId);
        User user = userService.getUserById(userId);

        List<PlanResponse> dtoList = plans.stream()
                .map(plan -> planMapper.planDTOtoPlanResponse(planMapper.planToPlanDTO(plan),plan.getUser().getNickname()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(DefaultRes.res(200, "success", dtoList));
    }

    @Operation(summary = "여행일정 추가", description = "여행일정을 추가하는 메서드입니다.")
    @PostMapping("/add")
    public ResponseEntity<DefaultRes<PlanResponse>> savePlan(@Valid @RequestBody PlanRequest reqDto) {
        PlanDTO dto = planMapper.planRequestToPlanDTO(reqDto,0,0,0);

        int userId = dto.getUserId();
        User user = userService.getUserById(userId);

        List<Integer> participants = dto.getParticipants();
        userService.getParticipantById(participants);

        //1. DTO -> 엔티티 변환
        Plan entity = planMapper.planDTOtoPlan(dto);
        entity.setUser(user);
        entity.setCreatedAt(LocalDateTime.now());
        //2. 엔티티 -> DB 저장
        Plan savedPlan = planService.save(entity);
        //3. 엔티티 -> DTO 변환
        PlanDTO savedDto = planMapper.planToPlanDTO(savedPlan);

        PlanResponse resDto = planMapper.planDTOtoPlanResponse(savedDto,user.getNickname());
        return ResponseEntity.ok(DefaultRes.res(201, "success", resDto));
    }

    @Operation(summary = "여행일정 수정", description = "여행일정을 수정하는 메서드입니다.")
    @PatchMapping("/add/{id}")
    public ResponseEntity<DefaultRes<PlanResponse>> updatePlan(@Valid @PathVariable int id, @RequestBody PlanRequest dto){
        Plan plan = planService.findById(id);
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
        if(dto.getIsPublic()!=null) plan.setIsPublic(dto.getIsPublic());

        Plan savedPlan = planService.save(plan);
        PlanDTO savedDto = planMapper.planToPlanDTO(savedPlan);
        PlanResponse resDto = planMapper.planDTOtoPlanResponse(savedDto,plan.getUser().getNickname());

        return ResponseEntity.ok(DefaultRes.res(200, "success", resDto));
    }

    @Operation(summary = "여행일정 삭제", description = "여행일정을 삭제하는 메서드입니다.")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<DefaultRes<Void>> deletePlanById(@PathVariable int id) {
        Plan plan = planService.findById(id);
        planService.deleteById(id);
        return ResponseEntity.ok(DefaultRes.res(204, "success"));
    }
}