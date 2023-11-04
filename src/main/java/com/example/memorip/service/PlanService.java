package com.example.memorip.service;

import com.example.memorip.entity.Plan;
import com.example.memorip.exception.CustomException;
import com.example.memorip.exception.ErrorCode;
import com.example.memorip.repository.PlanLikeRepository;
import com.example.memorip.repository.PlanRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;

@Slf4j
@Service
public class PlanService {
    private final PlanRepository planRepository;
    private final PlanLikeRepository planLikeRepository;
    public PlanService(PlanRepository planRepository,PlanLikeRepository planLikeRepository){
        this.planRepository=planRepository;
        this.planLikeRepository=planLikeRepository;
    }

    @Transactional
    public ArrayList<Plan> findAll(){
        return planRepository.getAllPlan();
    }

    @Transactional
    public ArrayList<Plan> sortByViews(){
        return planRepository.sortByViews();
    }

    @Transactional
    public ArrayList<Plan> sortByLikes(){
        return planRepository.sortByLikes();
    }

    @Transactional
    public Plan findById(int id){
        return planRepository.findById(id).orElseThrow(()->new CustomException(ErrorCode.PLAN_NOT_FOUND));
    }

    @Transactional
    public ArrayList<Plan> findByUserId(int userId){
        return planRepository.findByUserId(userId);
    }

    @Transactional
    public Plan save(Plan entity){
        return planRepository.save(entity);
    }

    @Transactional
    public void deleteById(int id) {
        Plan target = planRepository.findById(id).orElseThrow(()->new CustomException(ErrorCode.PLAN_NOT_FOUND));
        planLikeRepository.deleteByPlanId(id);
        planRepository.delete(target);
    }

    @Transactional
    public Plan findByUserIdAndId(int userId, int planId){
        return planRepository.findByUserIdAndId(userId,planId).orElseThrow(()->new CustomException(ErrorCode.USER_TRAVEL_NOT_FOUND));

    }

    @Transactional
    public Plan addParticipant(Plan plan, int userId){
        String participants = plan.getParticipants();
        String[] participantIds = participants.split(",");
        for (String id : participantIds) {
            if (id.equals(String.valueOf(userId))) {
                throw new CustomException(ErrorCode.DUPLICATE_RESOURCE, "이미 참여중입니다.");
            }
        }
        participants += "," + userId;
        plan.setParticipants(participants);
        return planRepository.save(plan);
    }

    @Transactional
    public Plan deleteParticipant(Plan plan, int userId){
        String participants = plan.getParticipants();
        String[] participantList = participants.split(",");
        StringBuilder newParticipants = new StringBuilder();
        for(String participant : participantList){
            if(!participant.equals(String.valueOf(userId))){
                newParticipants.append(participant).append(",");
            }
        }
        plan.setParticipants(newParticipants.toString());
        return planRepository.save(plan);
    }
}