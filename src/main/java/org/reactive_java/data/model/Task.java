package org.reactive_java.data.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Builder
@JsonSerialize
public class Task {

    private final long taskId;

    private final String taskNumber;

    private boolean isUserCreated = false;

    private final TaskPriority priority;

    private final List<TaskStatus> statuses;

    private final Duration completionTime;

    private final Evaluation evaluation;

    private final EvaluationDifference evaluationDifference;

    private User user;

    private String description;
}