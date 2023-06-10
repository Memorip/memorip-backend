package com.example.memorip.dto;

import lombok.Getter;
import lombok.Setter;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JwtResponseDTO { // JWT 토큰 응답 정보를 전달하기 위한 DTO

    private String token;
}