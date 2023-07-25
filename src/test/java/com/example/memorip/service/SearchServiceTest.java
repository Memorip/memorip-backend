package com.example.memorip.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SearchServiceTest {
    @Autowired
    private SearchService searchService;

    @Test
    public void searchTest() {
        var result = searchService.searchLocal("갈비집", "random");

        System.out.println(result);

        Assertions.assertNotNull(result);
    }
}