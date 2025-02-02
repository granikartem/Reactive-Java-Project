package org.reactive_java.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.Duration;
import java.util.Map;

@Data
@AllArgsConstructor
public class EvaluationDifference {
    boolean isFaster;

    Duration difference;

    Map<TaskStatus, StatusDifference> statusDifferences;
}

