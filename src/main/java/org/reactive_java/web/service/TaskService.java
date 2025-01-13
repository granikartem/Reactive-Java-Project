package org.reactive_java.web.service;

import io.reactivex.rxjava3.core.Flowable;
import jakarta.annotation.PostConstruct;
import org.reactive_java.data.generator.TaskGenerator;
import org.reactive_java.data.model.Task;
import org.reactive_java.data.model.User;
import org.reactive_java.web.dto.TaskDto;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class TaskService {
    private final List<User> users;
    AtomicBoolean running = new AtomicBoolean(false);
    AtomicBoolean addedTasksPresent = new AtomicBoolean(false);
    private Flowable<Task> taskFlowable;
    private long millisDelay = 5000;
    private Deque<TaskDto> addedTasks = new ArrayDeque<>();
    private Deque<Task> cache;

    public TaskService(List<User> users) {
        this.users = users;
    }

    @PostConstruct
    public void init() {
        System.out.println("task service init");
        running.set(true);
        cache = new ArrayDeque<>(100);
        TaskGenerator.setUsers(users);
        initFlowable();
    }

    private void initFlowable() {
        taskFlowable = Flowable.interval(millisDelay, TimeUnit.MILLISECONDS)
                .filter(_ -> running.get())
                .map(_ ->
                        {
                            Task task;
                            if (addedTasksPresent.get()) {
                                if (addedTasks.size() > 0) {
                                    var taskSpec = addedTasks.poll();
                                    task = TaskGenerator.generateTaskFromManipulation(
                                            taskSpec.getPriority(),
                                            taskSpec.getCompletionTime(),
                                            taskSpec.getEvaluation(),
                                            taskSpec.getUserName(),
                                            taskSpec.getDescription()
                                    );
                                    return task;
                                } else {
                                    addedTasksPresent.set(false);
                                }
                            }
                            return TaskGenerator.generateTask();
                        }
                )
                .doOnNext(this::addToCache);
    }

    private void addToCache(Task task) {
        if (cache.size() >= 100) {
            cache.poll();
        }
        cache.add(task);
    }

    public void pauseGeneration() {
        running.set(false);
    }

    public void resumeGeneration() {
        running.set(true);
    }

    public void updateDelay(long millisDelay) {
        this.millisDelay = millisDelay;
        taskFlowable.subscribe();
        initFlowable();
    }

    public long getDelay() {
        return millisDelay;
    }

    public void updateAverageCompletionTime(Duration duration) {
        TaskGenerator.setAverageCompletionTime(duration);
    }

    public Duration getAverageCompletionTime() {
        return TaskGenerator.getAverageCompletionTime();
    }

    public List<Task> getCachedTasks() {
        return cache.stream().toList();
    }

    public Flowable<Task> getTaskFlowable() {
        return taskFlowable;
    }
}
