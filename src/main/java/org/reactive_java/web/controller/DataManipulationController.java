package org.reactive_java.web.controller;

import org.reactive_java.web.dto.DelayDto;
import org.reactive_java.web.dto.DuratioDto;
import org.reactive_java.web.service.TaskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataManipulationController {
    final TaskService taskService;

    public DataManipulationController(TaskService taskService) {
        this.taskService = taskService;
        System.out.println(taskService);
    }

    @GetMapping("/data/manipulate/pause")
    public void pauseGeneration() {
        taskService.pauseGeneration();
    }

    @GetMapping("/data/manipulate/resume")
    public void resumeGeneration() {
        taskService.resumeGeneration();
    }

    @GetMapping("/data/manipulate/act")
    public DuratioDto getAverageCompletionTime() {
        return  new DuratioDto(taskService.getAverageCompletionTime());
    }

    @PostMapping("/data/manipulate/act")
    public void changeAverageCompletionTime(@RequestBody DuratioDto dto) {
        taskService.updateAverageCompletionTime(dto.toDuration());
    }

    @GetMapping("/data/manipulate/delay")
    public long getDelay() {
        return taskService.getDelay();
    }

    @PostMapping("/data/manipulate/delay")
    public void updateDelay(@RequestBody DelayDto delayDto) {
        taskService.updateDelay(delayDto.delay);
    }
}
