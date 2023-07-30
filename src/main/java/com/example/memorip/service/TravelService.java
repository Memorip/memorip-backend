package com.example.memorip.service;
import com.example.memorip.entity.Travel;
import com.example.memorip.exception.CustomException;
import com.example.memorip.exception.ErrorCode;
import com.example.memorip.repository.TravelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;

@Service
@Slf4j
public class TravelService {
    private final TravelRepository travelRepository;

    public TravelService(TravelRepository travelRepository){
        this.travelRepository=travelRepository;
    }

    @Transactional
    public ArrayList<Travel> findAllTravels(){
        return travelRepository.findAll();
    }

    @Transactional
    public ArrayList<Travel> travelSortByViews(){
        return travelRepository.sortByViews();
    }

    @Transactional
    public ArrayList<Travel> travelSortByLikes(){
        return travelRepository.sortByLikes();
    }

    @Transactional
    public ArrayList<Travel> travelSortByDate(int userId){
        return travelRepository.sortByDate(userId);
    }

    @Transactional
    public Travel findById(int id){
        return travelRepository.findById(id).orElseThrow(()
                ->new CustomException(ErrorCode.TRAVEL_NOT_FOUND));
    }


    @Transactional
    public ArrayList<Travel> findByUserId(int userId){
        return travelRepository.findByUserId(userId);
    }

    @Transactional
    public Travel save(Travel entity){
        return travelRepository.save(entity);
    }

    @Transactional
    public Travel deleteById(int id){
        Travel travel = travelRepository.findById(id).orElseThrow(()
                ->new CustomException(ErrorCode.TRAVEL_NOT_FOUND));
        travelRepository.delete(travel);
        return travel;
    }
}
