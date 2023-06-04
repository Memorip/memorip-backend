package com.example.memorip.repository;

import com.example.memorip.dto.PlanDTO;
import com.example.memorip.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface PlanRepository extends JpaRepository<Plan,Long> {

    @Override
    ArrayList<Plan> findAll();

    Plan findById(int id);


    @Override
    Plan save(Plan entity);


    /*
    default Plan save(PlanDTO dto){
        Plan entity = PlanMapper.INSTANCE.planDTOtoPlan(dto);
        return saveAndFlush(entity);
    };
     */


}
