package com.example.memorip.controller;


import com.example.memorip.dto.TimelineDTO;
import com.example.memorip.entity.Timeline;
import com.example.memorip.exception.DefaultRes;
import com.example.memorip.repository.TimelineMapper;
import com.example.memorip.service.TimelineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "timeline", description = "timeline api 입니다.")
@Slf4j
@RestController
@RequestMapping("/api/timelines")
@Validated
public class TimelineController {

    private final TimelineService timelineService;

    public TimelineController(TimelineService timelineService) {
        this.timelineService = timelineService;
    }


    @Operation(summary = "타임라인 목록 조회", description = "한 Plan에 대한 타임라인 목록을 조회합니다.")
    @GetMapping("")
    public ResponseEntity<DefaultRes<Map<String, List<TimelineDTO>>>> getTimelines(@RequestParam("planId") int planId) {
        List<Timeline> timelines = timelineService.findByPlanId(planId);
        Map<String, List<TimelineDTO>> timelineMap = groupTimelinesByDate(timelines);
        return new ResponseEntity<>(DefaultRes.res(200, "타임라인 목록 조회 성공", timelineMap), HttpStatus.OK);
    }

    private Map<String, List<TimelineDTO>> groupTimelinesByDate(List<Timeline> timelines){
        Map<String, List<TimelineDTO>> timelineMap = new HashMap<>();

        for(Timeline timeline : timelines){
            String date = String.valueOf(timeline.getDate()).substring(0, 10);
            log.info("date: " + date);
            if (timelineMap.containsKey(date)) {
                timelineMap.get(date).add(TimelineMapper.INSTANCE.timelineToTimelineDTO(timeline));
            } else {
                List<TimelineDTO> timelineList = new ArrayList<>();
                timelineList.add(TimelineMapper.INSTANCE.timelineToTimelineDTO(timeline));
                timelineMap.put(date, timelineList);
            }
        }

        return timelineMap;
    }

    @Operation(summary = "타임라인 조회", description = "타임라인을 한 개의 데이터를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<DefaultRes<TimelineDTO>> getTimeline(@PathVariable int id) {
        Timeline timeline = timelineService.findById(id);
        TimelineDTO timelineDTO = TimelineMapper.INSTANCE.timelineToTimelineDTO(timeline);
        return new ResponseEntity<>(DefaultRes.res(200, "타임라인 조회 성공", timelineDTO), HttpStatus.OK);
    }

    @Operation(summary = "타임라인 생성", description = "타임라인을 생성합니다.")
    @PostMapping("")
    public ResponseEntity<DefaultRes<List<TimelineDTO>>> saveTimeline(
            @Valid @RequestBody List<TimelineDTO> timelineDTOList
    ) {
        List<Timeline> createdTimelines = timelineService.saveAll(timelineDTOList);
        List<TimelineDTO> timelines = TimelineMapper.INSTANCE.timelinesToTimelineDTOs(createdTimelines);
        return new ResponseEntity<>(DefaultRes.res(201, "타임라인 생성 성공", timelines), HttpStatus.OK);
    }

    @Operation(summary = "타임라인 수정", description = "타임라인을 수정합니다.")
    @PatchMapping("/{id}")
    public ResponseEntity<DefaultRes<TimelineDTO>> updateTimeline(@PathVariable int id, @Valid @RequestBody TimelineDTO timelineDTO) {
        Timeline updatedTimeline = timelineService.updateById(id, timelineDTO);
        TimelineDTO updatedTimelineDTO = TimelineMapper.INSTANCE.timelineToTimelineDTO(updatedTimeline);

        return new ResponseEntity<>(DefaultRes.res(200, "타임라인 수정 성공", updatedTimelineDTO), HttpStatus.OK);
    }


    @Operation(summary = "타임라인 삭제", description = "타임라인을 삭제합니다.")
    @DeleteMapping("")
    public ResponseEntity<DefaultRes<Void>> deleteTimelines(@RequestParam("ids") List<Integer> ids) {
        timelineService.deleteByIds(ids);
        return new ResponseEntity<>(DefaultRes.res(204, "타임라인 삭제 성공"), HttpStatus.OK);
    }

}
