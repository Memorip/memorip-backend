package com.example.memorip.repository;

import com.example.memorip.dto.InvitationDTO;
import com.example.memorip.entity.Invitation;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface InvitationMapper{
    InvitationMapper INSTANCE = Mappers.getMapper(InvitationMapper.class);

    InvitationDTO invitationToInvitationDTO(Invitation invitation);
}
