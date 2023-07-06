package com.example.memorip.api;

import com.example.memorip.dto.SearchImageRequest;
import com.example.memorip.dto.SearchLocalRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class NaverAPITest {

    private final NaverAPI naverAPI;

    NaverAPITest(@Autowired NaverAPI naverAPI) {
        this.naverAPI = naverAPI;
    }

    @Test
    public void searchLocalTest() {

        var search = new SearchLocalRequest();
        search.setQuery("갈비집");
        search.setDisplay(5);

        var result = naverAPI.searchLocal(search);
        System.out.println(result);
    }

    @Test
    public void searchImageTest() {

        var search = new SearchImageRequest();
        search.setQuery("갈비집");

        var result = naverAPI.searchImage(search);
        System.out.println(result);
    }
}