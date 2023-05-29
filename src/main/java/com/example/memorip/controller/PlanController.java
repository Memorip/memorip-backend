package com.example.memorip.controller;

import com.example.memorip.dto.PlanDTO;
import com.example.memorip.entity.Plan;
import com.example.memorip.repository.PlanMapper;
import com.example.memorip.service.PlanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        public List<PlanDTO> slectAll(){
        List<Plan> lists = planService.selectAll();
        ArrayList<PlanDTO> dtoList = new ArrayList<>();

        for(Plan plan : lists){
            dtoList.add(planMapper.planToPlanDTO(plan));
        }
        return dtoList;
    }

}
