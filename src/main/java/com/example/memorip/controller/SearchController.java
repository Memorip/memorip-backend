package com.example.memorip.controller;

import com.example.memorip.dto.SearchLocalDTO;
import com.example.memorip.exception.DefaultRes;
import com.example.memorip.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "search", description = "search api 입니다.")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @Operation(summary = "지역 검색", description = "네이버 검색 api를 이용해 검색 결과를 return. 최대 5개의 결과를 내려주며, 검색 결과 정렬 방법을 참고\n" +
            "- random: 정확도순으로 내림차순 정렬(기본값)\n" +
            "- comment: 업체 및 기관에 대한 카페, 블로그의 리뷰 개수순으로 내림차순 정렬 ")
    @GetMapping("/searchLocal")
    public ResponseEntity<DefaultRes<List<SearchLocalDTO>>> searchLocal(@RequestParam String keyword){
        return new ResponseEntity<>(
                DefaultRes.res(200, "검색 성공",
                        searchService.searchLocal(keyword)), org.springframework.http.HttpStatus.OK);
    }

}
