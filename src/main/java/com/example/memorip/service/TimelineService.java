package com.example.memorip.service;

import com.example.memorip.dto.TimelineDTO;
import com.example.memorip.entity.Plan;
import com.example.memorip.entity.Timeline;
import com.example.memorip.repository.PlanRepository;
import com.example.memorip.repository.TimelineMapper;
import com.example.memorip.repository.TimelineRepository;
import com.example.memorip.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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
        if(plan==null) throw new NotFoundException("Plan not found with id: " + planId);

        Timeline timeline = TimelineMapper.INSTANCE.timelineDTOToTimeline(timelineDTO);
        timeline.setPlan(plan);
        timeline.setCreatedAt(LocalDateTime.now());

        return timelineRepository.save(timeline);
    }

    @Transactional(readOnly = true)
    public List<Timeline> findByPlanId(int planId){
        Plan plan = planRepository.findById(planId);
        if(plan==null) throw new NotFoundException("Plan not found with id: " + planId);

        return timelineRepository.findAllByPlanId(planId);
    }

    @Transactional(readOnly = true)
    public Timeline findOneById(int id){
        return timelineRepository.findById(id).orElseThrow(()->new NotFoundException("Timeline not found with id: " + id));
    }

    @Transactional
    public void deleteById(int id){
        timelineRepository.findById(id).orElseThrow(()->new NotFoundException("Timeline not found with id: " + id));
        timelineRepository.deleteById(id);
    }
}