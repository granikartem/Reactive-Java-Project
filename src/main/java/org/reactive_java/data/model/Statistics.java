package org.reactive_java.data.model;

import lombok.Data;
import org.reactive_java.web.dto.StatisticsDto;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class Statistics {
    String name;
    long taskCounter = 0;
    long onTimeCounter = 0;
    double doneOnTimePercentage = 0.0;
    Duration averageCompletionTime;
    Map<Status, Duration> averageStatusCompletionTime;
    Duration averageEvaluationDifference;
    Map<Status, Duration> averageStatusEvaluationDifference;

    public void update(
            boolean doneOnTime,
            Duration taskCompletionTime,
            Map<Status, Duration> taskStatusCompletionTime,
            Duration taskEvaluationDifference,
            Map<Status, Duration> taskStatusEvaluationDifference
    ) {
        taskCounter++;
        if (doneOnTime) {
            onTimeCounter++;
        }
        doneOnTimePercentage = (double) onTimeCounter / taskCounter;

        averageCompletionTime = recalculateAverageTime(averageCompletionTime, taskCompletionTime, taskCounter);

        if (averageStatusCompletionTime != null) {
            for (Map.Entry<Status, Duration> entry : averageStatusCompletionTime.entrySet()) {
                Duration newTime = recalculateAverageTime(
                        entry.getValue(),
                        taskStatusCompletionTime.get(entry.getKey()),
                        taskCounter
                );

                averageStatusCompletionTime.put(entry.getKey(), newTime);
            }
        } else {
            averageStatusCompletionTime = new HashMap<>();
            averageStatusCompletionTime.putAll(taskStatusCompletionTime);
        }

        averageEvaluationDifference = recalculateAverageTime(averageEvaluationDifference, taskEvaluationDifference, taskCounter);

        if (averageStatusEvaluationDifference != null) {
            for (Map.Entry<Status, Duration> entry : averageStatusEvaluationDifference.entrySet()) {
                Duration newTime = recalculateAverageTime(
                        entry.getValue(),
                        taskStatusEvaluationDifference.get(entry.getKey()),
                        taskCounter
                );

                averageStatusEvaluationDifference.put(entry.getKey(), newTime);
            }
        } else {
            averageStatusEvaluationDifference = new HashMap<>();
            averageStatusEvaluationDifference.putAll(taskStatusEvaluationDifference);
        }
    }

    private Duration recalculateAverageTime(Duration averageTime, Duration taskTime, long counter) {
        if (averageTime == null) {
            return taskTime;
        } else {
            return averageTime.plus(taskTime.minus(averageTime).dividedBy(counter));
        }
    }

    public StatisticsDto toDto() {
        return new StatisticsDto(
                taskCounter,
                doneOnTimePercentage,
                averageCompletionTime.toSeconds(),
                averageStatusCompletionTime.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().toSeconds())),
                averageEvaluationDifference.toSeconds(),
                averageStatusEvaluationDifference.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().toSeconds()))
        );
    }
}
