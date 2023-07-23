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

    @Operation(summary = "지역 검색", description = "네이버 api를 이용해 지역을 검색합니다.")
    @GetMapping("/searchLocal")
    public ResponseEntity<DefaultRes<List<SearchLocalDTO>>> searchLocal(@RequestParam String keyword, @RequestParam(defaultValue = "random") String sort){
        return new ResponseEntity<>(
                DefaultRes.res(200, "검색 성공",
                        searchService.searchLocal(keyword, sort)), org.springframework.http.HttpStatus.OK);
    }

}
