package com.example.memorip.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
public class InvitationRequest {

    @NotNull
    int planId;
}
