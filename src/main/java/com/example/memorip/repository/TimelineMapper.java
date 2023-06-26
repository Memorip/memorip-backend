package com.example.memorip.repository;

import com.example.memorip.dto.TimelineDTO;
import com.example.memorip.entity.Timeline;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TimelineMapper {
    TimelineMapper INSTANCE = Mappers.getMapper(TimelineMapper.class);

    List<TimelineDTO> timelinesToTimelineDTOs(List<Timeline> timelines);

    @Mapping(source = "plan.id", target = "planId")
    TimelineDTO timelineToTimelineDTO(Timeline timeline);

    Timeline timelineDTOToTimeline(TimelineDTO timelineDTO);
}
