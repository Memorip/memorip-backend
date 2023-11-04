package com.example.memorip.repository;

import com.example.memorip.dto.plan.PlanDTO;
import com.example.memorip.dto.plan.PlanRequest;
import com.example.memorip.dto.plan.PlanResponse;
import com.example.memorip.entity.Plan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PlanMapper {
    PlanMapper INSTANCE = Mappers.getMapper(PlanMapper.class);

    @Mapping(source = "city", target = "city",qualifiedByName = "cityToList")
    @Mapping(source = "participants", target = "participants",qualifiedByName = "participantsToIntegers")
    @Mapping(source = "user.id", target = "userId")
    PlanDTO planToPlanDTO(Plan plan);

    @Mapping(source = "city", target = "city",qualifiedByName = "cityListToString")
    @Mapping(source = "participants", target = "participants",qualifiedByName = "participantsIntegerToString")
    Plan planDTOtoPlan(PlanDTO dto);

    PlanResponse planDTOtoPlanResponse(PlanDTO dto, String nickname);

    PlanDTO planRequestToPlanDTO(PlanRequest request, int views, int likes, int id);

    @Named("cityToList")
    default List<String> citiesToLists(String city) {
        String[] lists = city.split(",");
        List<String> cities = new ArrayList<>(Arrays.asList(lists));
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
