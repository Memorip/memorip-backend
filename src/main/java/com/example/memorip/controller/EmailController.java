package com.example.memorip.controller;

import com.example.memorip.service.EmailServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/mail")
public class EmailController {

    private final EmailServiceImpl emailService;

    public EmailController(EmailServiceImpl emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/sendCode") // 이메일 인증코드 보내기
    @ResponseBody
    public ResponseEntity sendCode(@RequestParam("rcv") String emailRcv) throws Exception {
        log.info("rcv: " + emailRcv);
        emailService.sendVerificationCodeEmail(emailRcv);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/verifyCode") // 이메일 인증코드 확인
    @ResponseBody
    public ResponseEntity verifyCode(@RequestParam("rcv") String emailRcv, @RequestParam("code") String code) throws Exception {
        log.info("rcv: " + emailRcv);
        log.info("code: " + code);
        return emailService.verifyEmailCode(emailRcv, code)?
                ResponseEntity.ok(HttpStatus.OK) : ResponseEntity.badRequest().build();
    }


}
