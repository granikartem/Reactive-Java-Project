package org.reactive_java.web.service;

import io.reactivex.rxjava3.core.Flowable;
import jakarta.annotation.PostConstruct;
import org.reactive_java.data.generator.TaskGenerator;
import org.reactive_java.data.model.Task;
import org.reactive_java.data.model.User;
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
    private int millisDelay = 5000;
    private Deque<Task> cache;
    AtomicBoolean running = new AtomicBoolean(false);

    @PostConstruct
    public void init() {
        System.out.println("task service init");
        running.set(true);
        cache = new ArrayDeque<>(100);
        TaskGenerator.setUsers(users);
    }
    public TaskService(List<User> users) {
        this.users = users;
    }

    public void pauseGeneration() {
        running.set(false);
    }

    public void resumeGeneration() {
        running.set(true);
    }

    public void updateDelay(int millisDelay) {
        this.millisDelay = millisDelay;
    }

    public int getDelay() {
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
        return Flowable.interval(millisDelay, TimeUnit.MILLISECONDS)
                .filter(_ -> running.get())
                .map(_ -> {
                    var task = TaskGenerator.generateTask();
                    if (cache.size() < 100) {
                        cache.push(task);
                    } else {
                        cache.poll();
                        cache.push(task);
                    }
                    return task;
                });
    }
}
