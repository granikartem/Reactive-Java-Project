package org.reactive_java.web.service;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.processors.PublishProcessor;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.Getter;
import org.reactive_java.data.model.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class StatisticsService {
    private final Map<String, Group> groups;
    private final PublishProcessor<Statistics> statisticsProcessor = PublishProcessor.create();
    private final Map<User, Statistics> userStatistics = new ConcurrentHashMap<>();
    private final Map<Group, Statistics> groupStatistics = new ConcurrentHashMap<>();
    @Getter
    private final Statistics globalStatistics = new Statistics();
    private final TaskService taskService;
    private Disposable subscription;

    public StatisticsService(TaskService taskService, Map<String, Group> groups) {
        this.groups = groups;
        globalStatistics.setName("global");
        subscribe(taskService);
        this.taskService = taskService;
    }

    private void subscribe(TaskService taskService) {
        subscription = taskService.getTaskFlowable().subscribeOn(Schedulers.single()).subscribe(this::processTask);
    }

    private void processTask(Task task) {
        updateStatistics(globalStatistics, task);
        User user = task.getUser();
        userStatistics.computeIfAbsent(user, _ -> {
            Statistics stats = new Statistics();
            stats.setName("user" + user.getLogin());
            return stats;
        });
        updateStatistics(userStatistics.get(task.getUser()), task);

        for (String groupName : user.getGroups()) {
            Group group = groups.get(groupName);
            groupStatistics.computeIfAbsent(
                    group, _ -> {
                        Statistics stats = new Statistics();
                        stats.setName("group" + group.getName());
                        return stats;
                    }
            );
            updateStatistics(groupStatistics.get(group), task);
        }

        statisticsProcessor.onNext(globalStatistics);
        statisticsProcessor.onNext(userStatistics.get(user));
        for (String groupName : user.getGroups()) {
            Group group = groups.get(groupName);
            statisticsProcessor.onNext(groupStatistics.get(group));
        }
    }

    private void updateStatistics(Statistics statistics, Task task) {
        boolean doneOnTime = task.getEvaluationDifference().isFaster();
        Duration completionTime = task.getCompletionTime();
        Map<Status, Duration> statusCompletionTime = task.getStatuses()
                .stream()
                .collect(
                        Collectors.toMap(
                                TaskStatus::status,
                                TaskStatus::completionTime
                        )
                );
        Duration evaluationDifference = task.getEvaluationDifference().getDifference();
        if (task.getEvaluationDifference().isFaster()) {
            evaluationDifference = Duration.ZERO.minus(evaluationDifference);
        } else {
            evaluationDifference = Duration.ZERO.plus(evaluationDifference);
        }
        Map<Status, Duration> statusEvaluationDifference = task.getEvaluationDifference()
                .getStatusDifferences()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                                entry -> entry.getKey().status(),
                                entry -> {
                                    Duration diff = entry.getValue().getDifference();
                                    boolean isFaster = entry.getValue().isFaster();
                                    if (isFaster) {
                                        return Duration.ZERO.minus(diff);
                                    } else {
                                        return Duration.ZERO.plus(diff);
                                    }
                                }
                        )
                );


        statistics.update(
                doneOnTime,
                completionTime,
                statusCompletionTime,
                evaluationDifference,
                statusEvaluationDifference
        );
    }

    public void resubscribe() {
        subscription.dispose();
        subscribe(taskService);
    }

    public Flowable<Statistics> getStatisticsFlowable() {
        return statisticsProcessor;
    }

    public void unsubscribe() {
        subscription.dispose();
    }

    public Statistics getExistingUserStatistics(String userName) {
        return userStatistics.entrySet().stream().filter(e -> e.getKey().getLogin().equals(userName)).map(Map.Entry::getValue).findFirst().orElse(null);
    }

    public Statistics getExistingGroupStatistics(String groupName) {
        return groupStatistics.get(groups.getOrDefault(groupName, null));
    }


}