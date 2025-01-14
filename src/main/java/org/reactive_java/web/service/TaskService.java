package org.reactive_java.web.service;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import jakarta.annotation.PostConstruct;
import org.reactive_java.data.generator.TaskGenerator;
import org.reactive_java.data.model.Group;
import org.reactive_java.data.model.Task;
import org.reactive_java.data.model.User;
import org.reactive_java.web.dto.TaskDto;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static org.reactive_java.data.util.Utils.pickRandomGroups;

@Service
public class TaskService {
    private final List<User> users;
    private final Map<String, Group> groups;
    AtomicBoolean addedTasksPresent = new AtomicBoolean(false);
    private Flowable<Task> taskFlowable;
    private long millisDelay = 5000;
    private Deque<TaskDto> addedTasks = new ArrayDeque<>();
    private Deque<Task> cache;

    public TaskService(List<User> users, Map<String, Group> groups) {
        this.users = users;
        this.groups = groups;
    }

    @PostConstruct
    public void init() {
        System.out.println("task service init");
        cache = new ArrayDeque<>(100);
        TaskGenerator.setUsers(users);
        initFlowable();
    }

    private void initFlowable() {
        taskFlowable = Flowable.<Task>generate(emitter -> {
                    Task task;
                    if (!addedTasks.isEmpty()) {
                        task = generateTaskFromAdded(addedTasks);
                    } else {
                        task = TaskGenerator.generateTask();
                    }
                    emitter.onNext(task);
                    addToCache(task);
                }).zipWith(Flowable.interval(millisDelay, TimeUnit.MILLISECONDS), (task, _) -> task)
                .subscribeOn(Schedulers.single());
    }

    private Task generateTaskFromAdded(Deque<TaskDto> addedTasks) throws InterruptedException {
        var taskSpec = addedTasks.poll();
        return TaskGenerator.generateTaskFromManipulation(
                taskSpec.getPriority(),
                taskSpec.getCompletionTime(),
                taskSpec.getEvaluation(),
                new User(
                        taskSpec.getUserName(),
                        pickRandomGroups(groups.values().stream().toList())
                                .stream()
                                .map(Group::getName)
                                .collect(Collectors.toSet())
                ),
                taskSpec.getDescription()
        );
    }

    private void addToCache(Task task) {
        if (cache.size() >= 100) {
            cache.poll();
        }
        cache.add(task);
    }

    public void addTasksFromManipulation(int amount, TaskDto taskDto) {
        addedTasksPresent.set(true);
        for (int i = 0; i < amount; i++) {
            addedTasks.add(taskDto);
        }
    }

    public void pauseGeneration() {
        taskFlowable.subscribe().dispose();
//        running.set(false);
    }

    public void resumeGeneration() {
        initFlowable();
//        running.set(true);
    }

    public void updateDelay(long millisDelay) {
        pauseGeneration();
        this.millisDelay = millisDelay;
        resumeGeneration();
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
