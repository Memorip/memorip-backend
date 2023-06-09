package com.example.memorip.service;

import com.example.memorip.api.NaverAPI;
import com.example.memorip.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final NaverAPI naverAPI;

    public List<SearchLocalDTO> searchLocal(String query){

        SearchLocalRequest searchLocalRequest = new SearchLocalRequest();
        searchLocalRequest.setQuery(query);
        searchLocalRequest.setDisplay(5);

        SearchLocalResponse searchLocalResponse = naverAPI.searchLocal(searchLocalRequest);

        List<SearchLocalDTO> result = new ArrayList<>();

        if (searchLocalResponse.getTotal() > 0) {
            for(SearchLocalResponse.SearchLocalItem localItem : searchLocalResponse.getItems()){
                var imageQuery = localItem.getTitle().replaceAll("<[^>]*>", "");
                SearchImageRequest searchImageRequest = new SearchImageRequest();
                searchImageRequest.setQuery(imageQuery);

                SearchImageResponse searchImageResponse = naverAPI.searchImage(searchImageRequest);

                if (searchImageResponse.getTotal() > 0) {
                    var imageItem = searchImageResponse.getItems().stream().findFirst().orElse(null);

                    SearchLocalDTO localDTO = new SearchLocalDTO();
                    localDTO.setTitle(localItem.getTitle());
                    localDTO.setCategory(localItem.getCategory());
                    localDTO.setAddress(localItem.getAddress());
                    localDTO.setRoadAddress(localItem.getRoadAddress());
                    localDTO.setHomePageLink(localItem.getLink());
                    localDTO.setImageLink(imageItem != null ? imageItem.getLink() : null); // Null 값 설정
                    localDTO.setMapx(localItem.getMapx());
                    localDTO.setMapy(localItem.getMapy());

                    result.add(localDTO);
                }
            }
        }
        return result;

    }

}
