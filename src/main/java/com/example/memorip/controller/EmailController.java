package com.example.memorip.controller;

import com.example.memorip.service.EmailServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "email", description = "email 인증 api 입니다.")
@Slf4j
@RestController
@RequestMapping("/mail")
public class EmailController {

    private final EmailServiceImpl emailService;

    public EmailController(EmailServiceImpl emailService) {
        this.emailService = emailService;
    }


    @Operation(summary = "인증코드 전송", description = "이메일 인증코드를 전송하는 메서드입니다.")
    @GetMapping("/sendCode")
    @ResponseBody
    public ResponseEntity<?> sendCode(
            @Parameter(description = "인증받을 이메일", required = true)
            @RequestParam("rcv") String emailRcv) throws Exception {
        log.info("rcv: " + emailRcv);
        emailService.sendVerificationCodeEmail(emailRcv);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(summary = "인증코드 확인", description = "이메일 인증코드가 맞는지 검증하는 메서드입니다.")

    @GetMapping("/verifyCode")
    @ResponseBody
    public ResponseEntity<?> verifyCode(
            @Parameter(description = "인증받을 이메일", required = true)
            @RequestParam("rcv") String emailRcv,
            @Parameter(description = "인증코드", required = true)
            @RequestParam("code") String code) throws Exception {
        log.info("rcv: " + emailRcv);
        log.info("code: " + code);
        return emailService.verifyEmailCode(emailRcv, code)?
                ResponseEntity.ok(HttpStatus.OK) : ResponseEntity.badRequest().build();
    }
}
