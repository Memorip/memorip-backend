package com.example.memorip.service;

public interface EmailService {
    void sendVerificationCodeEmail(String rcv) throws Exception;
    boolean verifyEmailCode(String email, String code);
}
