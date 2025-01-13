package org.reactive_java.web.controller;

import io.reactivex.rxjava3.core.Flowable;
import org.reactive_java.data.model.Group;
import org.reactive_java.data.model.Task;
import org.reactive_java.web.dto.CachedTasksDto;
import org.reactive_java.web.service.TaskService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

@RestController
public class StatisticsController {
    private final TaskService taskService;
    private final List<Group> groups;

    public StatisticsController(List<Group> groups, TaskService taskService) {
        this.groups = groups;
        this.taskService = taskService;
    }

//    @GetMapping("/stats/test")
//    public void test() {
//        System.out.println("------------------------------------>test");
//    }

    @GetMapping(path = "/stats/test2", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamFlux() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(_ -> "Flux - " + LocalTime.now().toString());

    }

    @GetMapping(path = "/stats/tasks/new", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flowable<Task> emitTask() {
        return taskService.getTaskFlowable();
    }

    @GetMapping(path = "/stats/tasks/cached")
    public CachedTasksDto getCachedTasks() {
        return new CachedTasksDto(taskService.getCachedTasks());
    }

    @GetMapping(path = "/stats/test4/{num}")
    public String getGroup(@PathVariable("num") int num) {
        return groups.get(num).toString();
    }
}
