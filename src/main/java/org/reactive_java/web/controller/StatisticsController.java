package org.reactive_java.web.controller;

import io.reactivex.rxjava3.core.Flowable;
import org.reactive_java.data.model.Group;
import org.reactive_java.data.model.Statistics;
import org.reactive_java.data.model.Task;
import org.reactive_java.data.model.User;
import org.reactive_java.web.dto.CachedTasksDto;
import org.reactive_java.web.dto.StatisticsDto;
import org.reactive_java.web.service.StatisticsService;
import org.reactive_java.web.service.TaskService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class StatisticsController {
    private final List<User> users;
    private final Map<String, Group> groups;
    private final StatisticsService statisticsService;
    private final TaskService taskService;

    public StatisticsController(StatisticsService statisticsService, List<User> users, Map<String, Group> groups, TaskService taskService) {
        this.statisticsService = statisticsService;
        this.groups = groups;
        this.users = users;
        this.taskService = taskService;
    }

    @GetMapping(path ="/stats/users")
    public List<User> listAllUsers() {
        return users;
    }

    @GetMapping(path = "/stats/groups")
    public List<Group> listAllGroups() {
        return groups.values().stream().toList();
    }

    @GetMapping(path = "/stats/tasks/new", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flowable<Task> emitTask() {
        return taskService.getTaskFlowable();
    }

    @GetMapping(path = "/stats/tasks/cached")
    public CachedTasksDto getCachedTasks() {
        return new CachedTasksDto(taskService.getCachedTasks());
    }

    @GetMapping(path = "/stats/old/global")
    public Flux<StatisticsDto> getExistingStatistics() {
        return Flux.just(statisticsService.getGlobalStatistics().toDto());
    }

    @GetMapping(value = "/stats/global", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<StatisticsDto> getStatistics() {
        return Flux.from(
                statisticsService.getStatisticsFlowable()
                        .filter(e -> Objects.equals(e.getName(), "global"))
                        .map(Statistics::toDto).onBackpressureBuffer()
        );
    }

    @GetMapping(value = "/stats/users/{userName}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<StatisticsDto> getUserStatistics(@PathVariable String userName) {
        return Flux.from(
                statisticsService.getStatisticsFlowable()
                        .filter(e -> Objects.equals(e.getName(), "user" + userName))
                        .map(Statistics::toDto).onBackpressureBuffer()
        );
    }

    @GetMapping(path = "/stats/old/users/{userName}")
    public Flux<StatisticsDto> getExistingUserStatistics(@PathVariable String userName) {
        return Flux.just(statisticsService.getExistingUserStatistics(userName).toDto());
    }

    @GetMapping(value = "/stats/groups/{groupName}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<StatisticsDto> getGroupStatistics(@PathVariable String groupName) {
        return Flux.from(
                statisticsService.getStatisticsFlowable()
                .filter(e -> Objects.equals(e.getName(), "group" + groupName))
                        .map(Statistics::toDto).onBackpressureBuffer()
        );
    }

    @GetMapping(path = "/stats/old/groups/{groupName}")
    public Flux<StatisticsDto> getExistingGroupStatistics(@PathVariable String groupName) {
        return Flux.just(statisticsService.getExistingGroupStatistics(groupName).toDto());
    }
}
