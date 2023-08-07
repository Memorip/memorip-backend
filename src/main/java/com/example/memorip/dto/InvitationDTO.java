package com.example.memorip.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class InvitationDTO {
   private int id;
   private int planId;
   private String slug;
   private LocalDateTime createdAt;
   private LocalDateTime updatedAt;
}
