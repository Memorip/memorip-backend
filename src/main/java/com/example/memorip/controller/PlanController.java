package com.example.memorip.controller;

import com.example.memorip.dto.PlanDTO;
import com.example.memorip.entity.Plan;
import com.example.memorip.exception.DefaultRes;
import com.example.memorip.repository.PlanMapper;
import com.example.memorip.service.PlanService;
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
public class PlanController {
    private final PlanService planService;
    private final PlanMapper planMapper;


    PlanController(PlanService planService, PlanMapper planMapper){
        this.planService=planService;
        this.planMapper=planMapper;
    }

    @GetMapping("/plans")
    public ResponseEntity<?> slectAll(){
        List<Plan> lists = planService.getAll();
        ArrayList<PlanDTO> dtoList = new ArrayList<>();
        if (lists.size() == 0) {
            String errorMessage = "조회되는 여행 계획이 없어요.";
            return new ResponseEntity<>(DefaultRes.res(400, errorMessage, ""), HttpStatus.BAD_REQUEST);
        }
        for(Plan plan : lists){
            dtoList.add(planMapper.planToPlanDTO(plan));
        }
        return new ResponseEntity<>(DefaultRes.res(200, "success", dtoList), HttpStatus.OK);
    }

    @GetMapping("/plans/sort")
    public ResponseEntity<?> sortPlan(){
        List<Plan> lists = planService.sortByViews();
        ArrayList<PlanDTO> dtoList = new ArrayList<>();
        if (lists.size() == 0) {
            String errorMessage = "조회되는 여행 계획이 없어요.";
            return new ResponseEntity<>(DefaultRes.res(400, errorMessage, ""), HttpStatus.BAD_REQUEST);
        }
        for(Plan plan : lists){
            dtoList.add(planMapper.planToPlanDTO(plan));
        }
        return new ResponseEntity<>(DefaultRes.res(200, "success", dtoList), HttpStatus.OK);
    }

    @GetMapping("/plans/{id}")
    public ResponseEntity<?> getById(@PathVariable int id){
        Plan plan = planService.findById(id);
        if (plan == null) {
            String errorMessage = "조회되는 여행 계획이 없어요.";
            return new ResponseEntity<>(DefaultRes.res(400, errorMessage, ""), HttpStatus.BAD_REQUEST);
        }
        PlanDTO dto = planMapper.planToPlanDTO(plan);

        dto.setViews(dto.getViews()+1);
        Plan entity = planMapper.planDTOtoPlan(dto);
        Plan savedPlan = planService.save(entity);

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/plans/add")
    public ResponseEntity<?> savePlan(@Valid @RequestBody PlanDTO dto) {


        dto.setViews(0);
        //1. DTO -> 엔티티 변환
        Plan entity = planMapper.planDTOtoPlan(dto);

        //2. 엔티티 -> DB 저장
        Plan savedPlan = planService.save(entity);

        return new ResponseEntity<>(DefaultRes.res(200, "success", savedPlan), HttpStatus.OK);

    }

    @PatchMapping("/plans/add/{id}")
    public ResponseEntity<?> updatePlan(@Valid @PathVariable int id, @RequestBody PlanDTO dto){
        Plan plan = planService.findById(id);

        if(plan==null){
            String errorMessage = "수정할 여행 계획이 없어요.";
            return new ResponseEntity<>(DefaultRes.res(400, errorMessage, ""), HttpStatus.BAD_REQUEST);
        }

        if(dto.getCity()!=null) {
            String cities = planMapper.cityListToString(dto.getCity());
            plan.setCity(cities);
        }
        if(dto.getStartDate()!=null) plan.setStart_date(dto.getStartDate());
        if(dto.getEndDate()!=null) plan.setEnd_date(dto.getEndDate());
        if(dto.getTripType()!=null) plan.setTrip_type(dto.getTripType());
        if(dto.getParticipants()!=null) {
            String participants = planMapper.participantsIntegerToString(dto.getParticipants());
            plan.setParticipants(participants);
        }
        if(dto.getCreatedAt()!=null) plan.setCreated_at(dto.getCreatedAt());
        if(dto.getIsPublic()!=null) plan.setIs_public(true);


        Plan savedPlan = planService.save(plan);

        return new ResponseEntity<>(DefaultRes.res(200, "success",savedPlan), HttpStatus.OK);

   }

    @DeleteMapping("/plans/delete/{id}")
    public ResponseEntity<?> removeById(@PathVariable int id) {
        Plan removedPlan = planService.deleteById(id);
        if(removedPlan == null) {
            String errorMessage = "삭제할 여행 계획이 없어요.";
            return new ResponseEntity<>(DefaultRes.res(400, errorMessage, ""), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(DefaultRes.res(200, "success", null), HttpStatus.OK);
    }
}
