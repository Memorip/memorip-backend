package com.example.memorip.controller;


import com.example.memorip.dto.InvitationDTO;
import com.example.memorip.dto.InvitationRequest;
import com.example.memorip.entity.Invitation;
import com.example.memorip.exception.DefaultRes;
import com.example.memorip.repository.InvitationMapper;
import com.example.memorip.service.InvitationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "invitation", description = "초대링크 api 입니다.")
@Slf4j
@RestController
@RequestMapping("/api/invitations")
@Validated
public class InvitationController {
    private final InvitationService invitationService;

    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @Operation(summary = "초대링크 조회", description = "초대링크에 맞는 정보(plan)를 조회합니다.")
    @GetMapping("/{slug}")
    public ResponseEntity<DefaultRes<InvitationDTO>> getInvitation(@PathVariable String slug){
        Invitation invitation = invitationService.findOneBySlug(slug);
        InvitationDTO invitationDTO = InvitationMapper.INSTANCE.invitationToInvitationDTO(invitation);

        return new ResponseEntity<>(DefaultRes.res(200, "초대링크 조회 성공", invitationDTO), HttpStatus.OK);
    }

    @Operation(summary = "초대링크 생성", description = "랜덤한 문자열로 초대링크를 생성합니다.")
    @PostMapping("")
    public ResponseEntity<DefaultRes<InvitationDTO>> createInvitation(
            @Valid @RequestBody InvitationRequest request
    ) {
        Invitation invitation = invitationService.createOrUpdate(request.getPlanId());
        InvitationDTO invitationDTO = InvitationMapper.INSTANCE.invitationToInvitationDTO(invitation);

        return new ResponseEntity<>(DefaultRes.res(201, "초대링크 생성 성공", invitationDTO), HttpStatus.OK);
    }


}
