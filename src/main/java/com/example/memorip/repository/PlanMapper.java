package com.example.memorip.repository;

import com.example.memorip.dto.PlanDTO;
import com.example.memorip.entity.Plan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PlanMapper {

    PlanMapper INSTANCE = Mappers.getMapper(PlanMapper.class);

    @Mapping(source = "user_id", target = "userId")
    @Mapping(source = "city", target = "city",qualifiedByName = "cityToList")
    @Mapping(source = "start_date", target = "startDate")
    @Mapping(source = "end_date", target = "endDate")
    @Mapping(source = "trip_type", target = "tripType")
    @Mapping(source = "participants", target = "participants",qualifiedByName = "participantsToIntegers")
    @Mapping(source = "created_at", target = "createdAt")
    PlanDTO planToPlanDTO(Plan plan);

    @Mapping(source = "userId", target = "user_id")
    @Mapping(source = "city", target = "city",qualifiedByName = "cityListToString")
    @Mapping(source = "startDate", target = "start_date")
    @Mapping(source = "endDate", target = "end_date")
    @Mapping(source = "tripType", target = "trip_type")
    @Mapping(source = "participants", target = "participants",qualifiedByName = "participantsIntegerToString")
    @Mapping(source = "createdAt", target = "created_at")
    Plan planDTOtoPlan(PlanDTO dto);

    @Named("cityToList")
    default List<String> citiesToLists(String city) {
        List<String> cities = new ArrayList<>();
        cities.add(city);
        return cities;
    }

    @Named("participantsToIntegers")
    default List<Integer> participantsToIntegers(String participants) {
        List<Integer> participantIds = new ArrayList<>();
        String[] lists = participants.split(",");
        for (String list : lists) participantIds.add(Integer.parseInt(list));
        return participantIds;
    }

    @Named("cityListToString")
    default String cityListToString(List<String> city) {
        String answer= "";
        for (int i=0;i<city.size();i++) {
            if(i!=city.size()-1){
                answer += city.get(i) +",";
            }else{
                answer += city.get(i);
            }
        }
        return answer;
    }

    @Named("participantsIntegerToString")
    default String participantsIntegerToString(List<Integer> participants) {
        String answer = "";
        for (int i = 0; i < participants.size(); i++) {
            if (i != participants.size() - 1) {
                answer += participants.get(i) + ",";
            }else {
                answer += participants.get(i);
            }
        }
        return answer;
    }
}
