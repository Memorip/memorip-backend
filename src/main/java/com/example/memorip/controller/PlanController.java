package com.example.memorip.controller;

import com.example.memorip.dto.PlanDTO;
import com.example.memorip.entity.Plan;
import com.example.memorip.exception.DefaultRes;
import com.example.memorip.repository.PlanMapper;
import com.example.memorip.service.PlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class PlanController {
    private final PlanService planService;
    private final PlanMapper planMapper;


    PlanController(PlanService planService, PlanMapper planMapper){
        this.planService=planService;
        this.planMapper=planMapper;
    }

    @GetMapping("/plans")
    public ResponseEntity<?> slectAll(){
        List<Plan> lists = planService.selectAll();
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
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/plans/add")
    public ResponseEntity<?> savePlan(@RequestBody PlanDTO dto){

        //1. DTO -> 엔티티 변환
        Plan entity = planMapper.planDTOtoPlan(dto);

        //2. 엔티티 -> DB 저장
        Plan savedPlan = planService.save(entity);

        return new ResponseEntity<>(DefaultRes.res(200, "success", savedPlan), HttpStatus.OK);
    }

    @DeleteMapping("/plans/delete/{id}")
    public ResponseEntity<?> removeById(@PathVariable int id) {
        Plan plan = planService.findById(id);

        if (plan == null) {
            String errorMessage = "삭제할 여행 계획이 없어요.";
            return new ResponseEntity<>(DefaultRes.res(400, errorMessage, ""), HttpStatus.BAD_REQUEST);
        }

        Plan removedPlan = planService.deleteById(id);
        return new ResponseEntity<>(DefaultRes.res(200, "success", null), HttpStatus.OK);
    }
}
