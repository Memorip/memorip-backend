package com.example.memorip.service;

import com.example.memorip.entity.Invitation;
import com.example.memorip.entity.Plan;
import com.example.memorip.exception.CustomException;
import com.example.memorip.exception.ErrorCode;
import com.example.memorip.repository.InvitationRepository;
import com.example.memorip.repository.PlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class InvitationService {

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int LENGTH = 8;

    private final InvitationRepository invitationRepository;
    private final PlanRepository planRepository;

    public InvitationService(InvitationRepository invitationRepository, PlanRepository planRepository){
        this.invitationRepository = invitationRepository;
        this.planRepository = planRepository;
    }

    @Transactional(readOnly = true)
    public Invitation findOneBySlug(String slug){
        Invitation invitation = invitationRepository.findOneBySlug(slug).orElseThrow(()->new CustomException(ErrorCode.INVITATION_NOT_FOUND));
        LocalDateTime updatedAt = invitation.getUpdatedAt();

        if(updatedAt.isBefore(LocalDateTime.now().minusDays(1))){
            throw new CustomException(ErrorCode.INVITATION_EXPIRED);
        }
        return invitation;
    }

    @Transactional
    public Invitation createOrUpdate(Integer planId){
        Plan plan = planRepository.findById(planId).orElseThrow(()->
                new CustomException(ErrorCode.PLAN_NOT_FOUND));

        Optional<Invitation> invitation = invitationRepository.findOneByPlanId(planId);

        String slug = generateRandomString();
        Invitation newInvitation = invitation.orElseGet(() -> {
            Invitation invitation1 = new Invitation();
            invitation1.setCreatedAt(LocalDateTime.now());
            return invitation1;
        });

        newInvitation.setPlan(plan);
        newInvitation.setSlug(slug);
        newInvitation.setUpdatedAt(LocalDateTime.now());

        return invitationRepository.save(newInvitation);
    }

    public static String generateRandomString(){
        Random random = new Random();
        StringBuilder sb = new StringBuilder(LENGTH);

        for(int i=0;i<LENGTH;i++){
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }
}
