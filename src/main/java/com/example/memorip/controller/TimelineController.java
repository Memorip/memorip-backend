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

import java.util.List;

@Tag(name = "timeline", description = "timeline api 입니다.")
@Slf4j
@RestController
@RequestMapping("/api")
@Validated
public class TimelineController {

    private final TimelineService timelineService;

    public TimelineController(TimelineService timelineService) {
        this.timelineService = timelineService;
    }

    @Operation(summary = "타임라인 생성", description = "타임라인을 생성합니다.")
    @PostMapping("/timelines")
    public ResponseEntity<?> saveTimeline(
            @Valid @RequestBody TimelineDTO timelineDTO
    ) {
        Timeline createdTimeline = timelineService.save(timelineDTO);
        TimelineDTO timeline = TimelineMapper.INSTANCE.timelineToTimelineDTO(createdTimeline);
        return new ResponseEntity<>(DefaultRes.res(200, "타임라인 생성 성공", timeline), HttpStatus.OK);
    }

    @Operation(summary = "타임라인 목록 조회", description = "한 Plan에 대한 타임라인 목록을 조회합니다.")
    @GetMapping("/timelines")
    public ResponseEntity<?> getTimelines(@RequestParam("planId") int planId) {
        List<Timeline> timelines = timelineService.findByPlanId(planId);
        List<TimelineDTO> timelineDTOs = TimelineMapper.INSTANCE.timelinesToTimelineDTOs(timelines);
        return new ResponseEntity<>(DefaultRes.res(200, "타임라인 목록 조회 성공", timelineDTOs), HttpStatus.OK);
    }

    @Operation(summary = "타임라인 조회", description = "타임라인을 한 개의 데이터를 조회합니다.")
    @GetMapping("/timelines/{id}")
    public ResponseEntity<?> getTimeline(@PathVariable int id) {
        Timeline timeline = timelineService.findOneById(id);
        TimelineDTO timelineDTO = TimelineMapper.INSTANCE.timelineToTimelineDTO(timeline);
        return new ResponseEntity<>(DefaultRes.res(200, "타임라인 조회 성공", timelineDTO), HttpStatus.OK);
    }

    @Operation(summary = "타임라인 수정", description = "타임라인을 수정합니다.")
    @PatchMapping("/timelines/{id}")
    public ResponseEntity<?> updateTimeline(@PathVariable int id, @Valid @RequestBody TimelineDTO timelineDTO) {
        TimelineDTO timeline = TimelineMapper.INSTANCE.timelineToTimelineDTO(timelineService.findOneById(id));

        timeline.setDate(timelineDTO.getDate());
        timeline.setMemo(timelineDTO.getMemo());
        timeline.setData(timelineDTO.getData());

        TimelineDTO updatedTimeline = TimelineMapper.INSTANCE.timelineToTimelineDTO(timelineService.save(timeline));

        return new ResponseEntity<>(DefaultRes.res(200, "타임라인 수정 성공", updatedTimeline), HttpStatus.OK);
    }

    @Operation(summary = "타임라인 삭제", description = "타임라인을 삭제합니다.")
    @DeleteMapping("/timelines/{id}")
    public ResponseEntity<?> deleteTimeline(@PathVariable int id) {
        timelineService.deleteById(id);
        return new ResponseEntity<>(DefaultRes.res(200, "타임라인 삭제 성공"), HttpStatus.OK);
    }
}
