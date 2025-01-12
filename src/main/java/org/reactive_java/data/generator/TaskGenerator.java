package org.reactive_java.data.generator;

import lombok.Getter;
import org.apache.maven.surefire.shared.lang3.RandomStringUtils;
import org.reactive_java.data.model.*;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static org.reactive_java.data.util.Constants.*;

public class TaskGenerator {
    @Getter
    private static final Map<Group, Duration> averageGroupCompletionTime = new HashMap<>();
    @Getter
    private static final Map<User, Duration> averageUserCompletionTime = new HashMap<>();
    @Getter
    private static final Map<Status, Duration> averageStatusCompletionTime = Map.of(
            Status.WAITING, Duration.ofMinutes(30),
            Status.IN_PROGRESS, Duration.ofHours(3),
            Status.TESTING, Duration.ofHours(3),
            Status.INTEGRATION, Duration.ofMinutes(30)
    );
    private final static List<User> users = UserGenerator.generateUsers();
    @Getter
    private static Duration taskGenerationDelay = Duration.ofSeconds(1).plus(Duration.ofMillis(500));
    @Getter
    private static double averageCompletionRate = 0.75;
    @Getter
    private static Duration averageCompletionTime = Duration.ofHours(8);

    public static List<Task> generateTasks(int amount) {
        List<Task> tasks = new ArrayList<>(amount);

        for (int i = 0; i < amount; i++) {
            tasks.add(generateTask());
        }

        return tasks;
    }


    public static Task generateTask() {
        var statuses = generateStatuses();
        var completionTime = calculateCompletionTime(statuses);

        return new Task(
                generateId(),
                generateTaskNumber(),
                false,
                generateTaskPriority(),
                statuses,
                completionTime,
                generateEvaluation(),
                pickUser(),
                generateDescription()
        );
    }

    public static Task generateTaskFromManipulation(
            TaskPriority priority,
            List<TaskStatus> statuses,
            Duration completionTime,
            Evaluation evaluation,
            User user,
            String description
    ) {
        return new Task(
                generateId(),
                generateTaskNumber(),
                true,
                priority,
                statuses,
                completionTime,
                evaluation,
                user,
                description
        )
    }


    private static Long generateId() {
        return ThreadLocalRandom.current().nextLong();
    }

    private static String generateTaskNumber() {
        return RandomStringUtils.randomAlphanumeric(8);
    }

    private static TaskPriority generateTaskPriority() {
        return TaskPriority.values()[ThreadLocalRandom.current().nextInt(TaskPriority.values().length)];
    }

    private static List<TaskStatus> generateStatuses() {
        List<TaskStatus> statuses = new ArrayList<>(STATUS_AMOUNT);

        averageCompletionTime.
        return statuses;
    }

    private static Duration calculateCompletionTime(List<TaskStatus> statuses) {
    }

    private static Evaluation generateEvaluation() {
        Duration totalDuration = Duration.ZERO;
        Map<Status, Duration> statusDurationMap = new HashMap<>();

        for (int i = 0; i < STATUS_AMOUNT; i++) {
            Duration duration = generateDuration();
            totalDuration = totalDuration.plus(duration);
            statusDurationMap.put(Status.values()[i], duration);
        }

        return new Evaluation(totalDuration, statusDurationMap, pickUser());
    }


    private static User pickUser() {
        return users.get(ThreadLocalRandom.current().nextInt(USER_AMOUNT));
    }

    private static String generateDescription() {
        return RandomStringUtils.randomAlphanumeric(100);
    }

    private static Duration generateDuration() {
        return Duration.of(ThreadLocalRandom.current().nextLong(MAX_DURATION.toSeconds()), ChronoUnit.SECONDS);
    }

    public static void setTaskGenerationDelay(Duration taskGenerationDelay) {
        TaskGenerator.taskGenerationDelay = taskGenerationDelay;
    }

    public static void setAverageCompletionRate(double averageCompletionRate) {
        TaskGenerator.averageCompletionRate = averageCompletionRate;
    }

    public static void setAverageCompletionTime(Duration duration) {
        TaskGenerator.averageCompletionTime = duration;
    }

    public static void setGroupAverageCompletionTime(Group group, Duration duration) {
        TaskGenerator.averageGroupCompletionTime.put(group, duration);
    }

    public static void setUserAverageCompletionTime(User user, Duration duration) {
        TaskGenerator.averageUserCompletionTime.put(user, duration);
    }
}
