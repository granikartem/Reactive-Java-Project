package org.reactive_java.web.controller;

import org.reactive_java.web.dto.AddTasksDto;
import org.reactive_java.web.dto.DelayDto;
import org.reactive_java.web.dto.DuratioDto;
import org.reactive_java.web.service.StatisticsService;
import org.reactive_java.web.service.TaskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataManipulationController {
    final StatisticsService statisticsService;
    final TaskService taskService;

    public DataManipulationController(StatisticsService statisticsService, TaskService taskService) {
        this.statisticsService = statisticsService;
        this.taskService = taskService;
        System.out.println(taskService);
    }

    @GetMapping("/data/manipulate/pause")
    public void pauseGeneration() {
        taskService.pauseGeneration();
        statisticsService.unsubscribe();
    }

    @GetMapping("/data/manipulate/resume")
    public void resumeGeneration() {
        taskService.resumeGeneration();
        statisticsService.resubscribe();
    }

    @GetMapping("/data/manipulate/act")
    public DuratioDto getAverageCompletionTime() {
        return new DuratioDto(taskService.getAverageCompletionTime());
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
        statisticsService.resubscribe();
    }

    @PostMapping("/data/manipulate/tasks")
    public void addTasks(@RequestBody AddTasksDto addTasksDto) {
        taskService.addTasksFromManipulation(addTasksDto.getAmount(), addTasksDto.getTaskSpecification());
    }

}
