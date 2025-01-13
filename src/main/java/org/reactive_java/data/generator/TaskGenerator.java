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
    @Getter
    private static List<User> users;
    @Getter
    private static final Duration averageEvaluationTime = Duration.ofHours(10);
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

//    public static List<Task> generateTasksFromManipulation(
//            int amount,
//            TaskPriority priority,
//            Duration completionTime,
//            Duration evaluationTime,
//            String userName,
//            String description
//    ) {
//        List<Task> tasks = new ArrayList<>(amount);
//
//        for (int i = 0; i < amount; i++) {
//            tasks.add(generateTaskFromManipulation(priority, completionTime, evaluation, user, description));
//        }
//
//        return tasks;
//    }


    public static Task generateTask() {
        var statuses = generateStatuses();
        var completionTime = calculateCompletionTime(statuses);
        var evaluation = generateEvaluation();
        var evaluationDifference = calculateDifference(statuses, evaluation);

        return new Task(
                generateId(),
                generateTaskNumber(),
                false,
                generateTaskPriority(),
                statuses,
                completionTime,
                evaluation,
                evaluationDifference,
                pickUser(),
                generateDescription()
        );
    }

    public static Task generateTaskFromManipulation(
            TaskPriority priority,
            Duration completionTime,
            Duration evaluationTime,
            String userName,
            String description
    ) {
        List<TaskStatus> statuses = generateStatusesFromTime(completionTime);
        Evaluation evaluation = generateEvaluationFromTime(evaluationTime);
        User user = UserGenerator.generateUserFromUserName(userName);

        return new Task(
                generateId(),
                generateTaskNumber(),
                true,
                priority,
                statuses,
                completionTime,
                evaluation,
                calculateDifference(statuses, evaluation),
                user,
                description
        );
    }

    private static Evaluation generateEvaluationFromTime(Duration evaluationTime) {
        return null;
    }

    private static List<TaskStatus> generateStatusesFromTime(Duration completionTime) {
        return null;
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

        for (int i = 0; i < STATUS_AMOUNT; i++) {
            Status status = Status.values()[i];
            double statusShare = STATUS_TIME_SHARE_MAP.get(status);
            long averageMillis = (long) (averageCompletionTime.toMillis() * statusShare);

            Duration statusDuration = generateDurationFromMeanMillis(averageMillis);

            TaskStatus taskStatus = new TaskStatus(status, statusDuration);
            statuses.add(taskStatus);
        }
        return statuses;
    }

    private static Duration generateDurationFromMeanMillis(long averageMillis) {
        double ratio = 1.0 + ThreadLocalRandom.current().nextGaussian();
        if (ratio < 0.0001) {
            return Duration.ofMillis((long) (averageMillis * 0.1));
        } else {
            return Duration.ofMillis((long) (averageMillis * ratio));
        }
    }

    private static Duration calculateCompletionTime(List<TaskStatus> statuses) {
        Duration totalDuration = Duration.ZERO;

        for (TaskStatus status : statuses) {
            totalDuration = totalDuration.plus(status.completionTime());
        }

        return totalDuration;
    }

    private static Evaluation generateEvaluation() {
        Duration totalDuration = Duration.ZERO;
        Map<Status, Duration> statusDurationMap = new HashMap<>();

        for (int i = 0; i < STATUS_AMOUNT; i++) {
            Status status = Status.values()[i];
            double statusShare = STATUS_TIME_SHARE_MAP.get(status);
            long averageMillis = (long) (averageEvaluationTime.toMillis() * statusShare);

            Duration statusDuration = generateDurationFromMeanMillis(averageMillis);

            totalDuration = totalDuration.plus(statusDuration);
            statusDurationMap.put(status, statusDuration);
        }

        return new Evaluation(totalDuration, statusDurationMap, pickUser());
    }

    private static EvaluationDifference calculateDifference(List<TaskStatus> statuses, Evaluation evaluation) {
        Duration completionTime = calculateCompletionTime(statuses);

        Duration difference;
        boolean faster;

        if (completionTime.compareTo(evaluation.getEstimatedCompletionTime()) < 0) {
            difference = evaluation.getEstimatedCompletionTime().minus(completionTime);
            faster = true;
        } else {
            difference = completionTime.minus(evaluation.getEstimatedCompletionTime());
            faster = false;
        }

        Map<TaskStatus, StatusDifference> statusDifferences = new HashMap<>();

        for (TaskStatus status : statuses) {
            var statusCompletionTime = status.completionTime();
            var statusEvaluationTime = evaluation.getStatusDurationMap().get(status.status());
            StatusDifference statusDifference;

            if (statusCompletionTime.compareTo(statusEvaluationTime) < 0) {
                statusDifference = new StatusDifference(
                        true,
                        statusEvaluationTime.minus(statusCompletionTime)
                );
            } else {
                statusDifference = new StatusDifference(
                        false,
                        statusCompletionTime.minus(statusEvaluationTime)
                );
            }

            statusDifferences.put(status, statusDifference);
        }

        return new EvaluationDifference(faster, difference, statusDifferences);
    }

    public static void setUsers(List<User> users) {
        TaskGenerator.users = users;
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
