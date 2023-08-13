package com.example.memorip.controller;

import com.example.memorip.dto.ReqPlanDTO;
import com.example.memorip.dto.ResPlanDTO;
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

    @Operation(summary = "여행일정 전체 조회", description = "여행일정을 전체 조회하는 메서드입니다.")
    @GetMapping("")
    public ResponseEntity<DefaultRes<List<ResPlanDTO>>> getPlans(){
        List<Plan> lists = planService.findAll();
        ArrayList<ResPlanDTO> dtoList = new ArrayList<>();
        for(Plan plan : lists){
            ResPlanDTO resDto = planMapper.planToPlanDTO(plan);
            String nickname = plan.getUser().getNickname();
            resDto.setNickname(nickname);
            dtoList.add(resDto);
        }
        return new ResponseEntity<>(DefaultRes.res(200, "success", dtoList), HttpStatus.OK);
    }

    // 조회수 순 정렬
    @Operation(summary = "여행일정 조회순 정렬", description = "여행일정을 조회순으로 정렬하여 조회하는 메서드입니다.")
    @GetMapping("/view/sort")
    public ResponseEntity<DefaultRes<List<ResPlanDTO>>> sortPlanByViews(){
        List<Plan> lists = planService.sortByViews();
        ArrayList<ResPlanDTO> dtoList = new ArrayList<>();
        if(lists.size()>0){
            for(Plan plan : lists){
                ResPlanDTO resDto = planMapper.planToPlanDTO(plan);
                String nickname = plan.getUser().getNickname();
                resDto.setNickname(nickname);
                dtoList.add(resDto);
            }
        }
        return new ResponseEntity<>(DefaultRes.res(200, "success", dtoList), HttpStatus.OK);
    }

    // 좋아요 순 정렬
    @Operation(summary = "여행일정 좋아요순 정렬", description = "여행일정을 좋아요순으로 정렬하여 조회하는 메서드입니다.")
    @GetMapping("/like/sort")
    public ResponseEntity<DefaultRes<List<ResPlanDTO>>> sortPlanByLikes(){
        List<Plan> lists = planService.sortByLikes();
        ArrayList<ResPlanDTO> dtoList = new ArrayList<>();
        if (lists.size() == 0) {
            String errorMessage = "조회되는 여행 계획이 없어요.";
            return new ResponseEntity<>(DefaultRes.res(400, errorMessage, null), HttpStatus.BAD_REQUEST);
        }
        for(Plan plan : lists){
            ResPlanDTO resDto = planMapper.planToPlanDTO(plan);
            String nickname = plan.getUser().getNickname();
            resDto.setNickname(nickname);
            dtoList.add(resDto);
        }
        return new ResponseEntity<>(DefaultRes.res(200, "success", dtoList), HttpStatus.OK);
    }

    @Operation(summary = "여행일정 상세조회", description = "여행일정 상세를 조회하는 메서드입니다.")
    @GetMapping("/{id}")
    public ResponseEntity<DefaultRes<ResPlanDTO>> getPlanById(@PathVariable int id){
        Plan plan = planService.findById(id);
        String nickname = plan.getUser().getNickname();

        if(!plan.getIsPublic()){
            throw new CustomException(ErrorCode.ACCESS_DENIED_PLAN);
        }

        ResPlanDTO dto = planMapper.planToPlanDTO(plan);

        dto.setViews(dto.getViews()+1);
        dto.setNickname(nickname);
        Plan entity = planMapper.planDTOtoPlan(dto);
        int userId = dto.getUserId();
        User user = userService.getUserById(userId);
        entity.setUser(user);

        Plan savedPlan = planService.save(entity);
        return new ResponseEntity<>(DefaultRes.res(200, "success", dto), HttpStatus.OK);
    }

    @Operation(summary = "유저별 여행일정 조회", description = "유저별로 여행일정을 조회하는 메서드입니다.")
    @GetMapping("/user/{userId}")
    public ResponseEntity<DefaultRes<List<ResPlanDTO>>> getPlanByUserId(@PathVariable int userId){
        List<Plan> lists = planService.findByUserId(userId);
        User user = userService.getUserById(userId);

        ArrayList<ResPlanDTO> dtoList = new ArrayList<>();
        for(Plan plan : lists){
            ResPlanDTO resDto = planMapper.planToPlanDTO(plan);
            String nickname = plan.getUser().getNickname();
            resDto.setNickname(nickname);
            dtoList.add(resDto);
        }
        return new ResponseEntity<>(DefaultRes.res(200, "success", dtoList), HttpStatus.OK);
    }

    @Operation(summary = "여행일정 추가", description = "여행일정을 추가하는 메서드입니다.")
    @PostMapping("/add")
    public ResponseEntity<DefaultRes<ResPlanDTO>> savePlan(@Valid @RequestBody ReqPlanDTO dto) {
        dto.setViews(0);

        int userId = dto.getUserId();
        User user = userService.getUserById(userId);
        String nickname = user.getNickname();

        List<Integer> participants = dto.getParticipants();
        userService.getParticipantById(participants);

        ResPlanDTO resDto = new ResPlanDTO();
        resDto.setId(dto.getId());
        resDto.setUserId(dto.getUserId());
        resDto.setNickname(nickname);
        resDto.setCity(dto.getCity());
        resDto.setStartDate(dto.getStartDate());
        resDto.setEndDate(dto.getEndDate());
        resDto.setTripType(dto.getTripType());
        resDto.setParticipants(dto.getParticipants());
        resDto.setCreatedAt(dto.getCreatedAt());
        resDto.setIsPublic(dto.getIsPublic());
        resDto.setViews(dto.getViews());
        resDto.setLikes(dto.getLikes());

        //1. DTO -> 엔티티 변환
        Plan entity = planMapper.planDTOtoPlan(resDto);
        entity.setUser(user);
        entity.setCreatedAt(LocalDateTime.now());
        //2. 엔티티 -> DB 저장
        Plan savedPlan = planService.save(entity);
        //3. 엔티티 -> DTO 변환
        ResPlanDTO savedDto = planMapper.planToPlanDTO(savedPlan);
        savedDto.setNickname(nickname);

        return new ResponseEntity<>(DefaultRes.res(201, "success", savedDto), HttpStatus.OK);
    }

    @Operation(summary = "여행일정 수정", description = "여행일정을 수정하는 메서드입니다.")
    @PatchMapping("/add/{id}")
    public ResponseEntity<DefaultRes<ResPlanDTO>> updatePlan(@Valid @PathVariable int id, @RequestBody ReqPlanDTO dto){
        Plan plan = planService.findById(id);
        String nickname = plan.getUser().getNickname();
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
        if(dto.getIsPublic()!=null) plan.setIsPublic(dto.getIsPublic());

        Plan savedPlan = planService.save(plan);
        ResPlanDTO savedDto = planMapper.planToPlanDTO(savedPlan);
        savedDto.setNickname(nickname);
        return new ResponseEntity<>(DefaultRes.res(200, "success",savedDto), HttpStatus.OK);
    }

    @Operation(summary = "여행일정 삭제", description = "여행일정을 삭제하는 메서드입니다.")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<DefaultRes<Void>> deletePlanById(@PathVariable int id) {
        Plan plan = planService.findById(id);
        planService.deleteById(id);
        return new ResponseEntity<>(DefaultRes.res(204, "success"), HttpStatus.OK);
    }
}