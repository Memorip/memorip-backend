package com.example.memorip.controller;

import com.example.memorip.dto.S3DTO;
import com.example.memorip.exception.DefaultRes;
import com.example.memorip.service.S3UploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "s3", description = "s3 업로드 관련 api 입니다.")
@Slf4j
@RestController
@RequestMapping("/api/s3")
@Validated
public class S3UploadController {
    private final S3UploadService s3UploadService;

    public S3UploadController(S3UploadService s3UploadService) {
        this.s3UploadService = s3UploadService;
    }

    @Operation(summary = "이미지 url 확인", description = "이미지 url을 불러오는 메서드입니다.")
    @PostMapping("/url")
    public ResponseEntity<DefaultRes<S3DTO>> getUrl(@RequestPart(value = "url") MultipartFile url) {
        S3DTO dto = new S3DTO();
        // 이미지 업로드 및 URL 저장
        String imgUrl = null;
        if (url != null && !url.isEmpty()) {
            try {
                imgUrl = s3UploadService.saveFile(url); // S3에 업로드하고 URL 반환
            } catch (IOException e) {
                // 이미지 업로드 실패 처리
                e.printStackTrace();
                // 에러 응답을 보낼 수 있습니다.
            }
        }else{
            String errorMessage = "요청하신 이미지 파일이 비었어요.";
            return new ResponseEntity<>(DefaultRes.res(400, errorMessage, null), HttpStatus.BAD_REQUEST);
        }

        dto.setUrl(imgUrl);
        return ResponseEntity.ok(DefaultRes.res(200, "success", dto));
    }
}
