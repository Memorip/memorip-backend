package com.example.memorip.service;

import com.example.memorip.dto.TimelineDTO;
import com.example.memorip.entity.Plan;
import com.example.memorip.entity.Timeline;
import com.example.memorip.exception.CustomException;
import com.example.memorip.exception.ErrorCode;
import com.example.memorip.repository.PlanRepository;
import com.example.memorip.repository.TimelineMapper;
import com.example.memorip.repository.TimelineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TimelineService {

    private final TimelineRepository timelineRepository;
    private final PlanRepository planRepository;

    public TimelineService(TimelineRepository timelineRepository, PlanRepository planRepository){
        this.timelineRepository=timelineRepository;
        this.planRepository=planRepository;
    }

    @Transactional
    public Timeline save(TimelineDTO timelineDTO){
        int planId = timelineDTO.getPlanId();
        Plan plan = planRepository.findById(planId);
        if(plan==null) throw new CustomException(ErrorCode.PLAN_NOT_FOUND);

        Timeline timeline = TimelineMapper.INSTANCE.timelineDTOToTimeline(timelineDTO);
        timeline.setPlan(plan);
        timeline.setCreatedAt(LocalDateTime.now());

        return timelineRepository.save(timeline);
    }

    @Transactional(readOnly = true)
    public List<Timeline> findByPlanId(int planId){
        Plan plan = planRepository.findById(planId);
        if(plan==null) throw new CustomException(ErrorCode.PLAN_NOT_FOUND);

        return timelineRepository.findAllByPlanId(planId);
    }

    @Transactional(readOnly = true)
    public Timeline findOneById(int id){
        return timelineRepository.findById(id).orElseThrow(()->new CustomException(ErrorCode.TIMELINE_NOT_FOUND));
    }

    @Transactional
    public void deleteById(int id){
        Optional<Timeline> timeline = timelineRepository.findById(id);
        if(timeline.isEmpty()){
            throw new CustomException(ErrorCode.TIMELINE_NOT_FOUND);
        }
        timelineRepository.deleteById(id);
    }

}