package org.reactive_java.web.controller;

import org.reactive_java.web.dto.ManipulationDelayDto;
import org.reactive_java.web.service.TaskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataManipulationController {
    final
    TaskService taskService;

    public DataManipulationController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/data/manipulate/pause")
    public void pauseGeneration() {
        taskService.pauseGeneration();
    }

    @GetMapping("/data/manipulate/resume")
    public void resumeGeneration() {
        taskService.resumeGeneration();
    }

    @PostMapping("/data/manipulate/delay")
    public void changeGenerationDelay(ManipulationDelayDto dto) {
        taskService.updateAverageCompletionTime(dto.toDuration());
    }
}
