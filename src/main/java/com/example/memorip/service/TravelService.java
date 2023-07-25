package com.example.memorip.service;
import com.example.memorip.entity.Travel;
import com.example.memorip.repository.TravelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Optional;

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
    public Travel findById(int id){
        return travelRepository.findById(id);
    }

    @Transactional
    public Travel save(Travel entity){
        return travelRepository.save(entity);
    }

    @Transactional
    public Travel deleteById(int id){
        Optional<Travel> optionalTravel = Optional.ofNullable(travelRepository.findById(id));
        if (optionalTravel.isEmpty()) {
            return null;
        }
        Travel target = optionalTravel.get();
        travelRepository.delete(target);
        return target;
    }
}
