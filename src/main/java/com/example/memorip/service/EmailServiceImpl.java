package com.example.memorip.service;

import com.example.memorip.exception.CustomException;
import com.example.memorip.exception.ErrorCode;
import com.example.memorip.util.RedisUtil;
import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {
    // 문자 유형을 나타내는 상수 값
    private static final int DIGIT = 0;
    private static final int LOWER_CASE = 1;
    private static final int UPPER_CASE = 2;

    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;

    public EmailServiceImpl(JavaMailSender javaMailSender, RedisUtil redisUtil) {
        this.javaMailSender = javaMailSender;
        this.redisUtil = redisUtil;
    }

    @Override
    public void sendVerificationCodeEmail(String rcv) throws Exception {
        String emailCode = generateAuthCode(4);

        MimeMessage message = createMessage(rcv, emailCode);
        if(redisUtil.existData(rcv)){
            redisUtil.deleteData(rcv);
        }
        try{
            javaMailSender.send(message);
        }catch (MailException e){
            log.info("이메일 전송 실패", e);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR, "이메일 전송 실패");
        }
        redisUtil.setDataExpire(rcv, emailCode, 60*5L);
    }

    @Override
    public boolean verifyEmailCode(String email, String code) {
        String codeFoundByEmail = redisUtil.getData(email);
        return codeFoundByEmail != null && codeFoundByEmail.equals(code);
    }

    private MimeMessage createMessage(String recipient, String emailCode) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, recipient);
        message.setSubject("이메일 인증 코드");

        String mailMsgBuilder = "<div>" +
                "<h1>이메일 인증 코드</h1>" +
                "<p>아래 코드를 입력하여 이메일 인증을 완료해주세요.</p>" +
                "CODE: <strong>" +
                emailCode +
                "</strong></div>";

        message.setText(mailMsgBuilder, "utf-8", "html");
        message.setFrom(new InternetAddress("nhj7911@gmail.com", "Memorip"));

        return message;
    }

    public static String generateAuthCode(int codeLength) {
        return generateAuthCodeInternal(
                ThreadLocalRandom.current().ints(codeLength, 0, 3).boxed()
        );
    }

    private static String generateAuthCodeInternal(Stream<Integer> typeStream) {
        return typeStream.map(type -> switch (type) {
            case DIGIT -> Character.forDigit(
                    ThreadLocalRandom.current().nextInt(10), 10
            );
            case LOWER_CASE -> (char) (ThreadLocalRandom.current().nextInt(26) + 'a');
            case UPPER_CASE -> (char) (ThreadLocalRandom.current().nextInt(26) + 'A');
            default -> '0';
        }).map(String::valueOf).collect(Collectors.joining());
    }
}
