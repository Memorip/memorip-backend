package com.example.memorip.api;

import com.example.memorip.dto.SearchImageRequest;
import com.example.memorip.dto.SearchImageResponse;
import com.example.memorip.dto.SearchLocalRequest;
import com.example.memorip.dto.SearchLocalResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class NaverAPI {
    private final String clientId;
    private final String clientSecret;
    private final String localSearchUrl;
    private final String imageSearchUrl;

    public NaverAPI(@Value("${naver.client.id}") String clientId,
                    @Value("${naver.client.secret}") String clientSecret,
                    @Value("${naver.url.search.local}") String localSearchUrl,
                    @Value("${naver.url.search.image}") String imageSearchUrl) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.localSearchUrl = localSearchUrl;
        this.imageSearchUrl = imageSearchUrl;
    }

    public SearchLocalResponse searchLocal(SearchLocalRequest searchLocalRequest) {
        RestTemplate restTemplate = new RestTemplate();

        var uri = UriComponentsBuilder
                .fromUriString(localSearchUrl)
                .queryParams(searchLocalRequest.toMultiValueMap())
                .build()
                .encode()
                .toUri();

        HttpHeaders headers = createHeaders();
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<SearchLocalResponse> responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                httpEntity,
                SearchLocalResponse.class
        );
        return responseEntity.getBody();
    }

    public SearchImageResponse searchImage(SearchImageRequest searchImageRequest) {
        RestTemplate restTemplate = new RestTemplate();

        var uri = UriComponentsBuilder
                .fromUriString(imageSearchUrl)
                .queryParams(searchImageRequest.toMultiValueMap())
                .build()
                .encode()
                .toUri();

        HttpHeaders headers = createHeaders();
        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<SearchImageResponse> responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                httpEntity,
                SearchImageResponse.class
        );

        return responseEntity.getBody();
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
