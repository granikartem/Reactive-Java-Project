package org.reactive_java.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.reactive_java.data.model.Status;

import java.util.Map;

@Data
@AllArgsConstructor
public class StatisticsDto {
    long taskCounter;
    double doneOnTimePercentage;
    long averageCompletionTime;
    Map<Status, Long> averageStatusCompletionTime;
    long averageEvaluationDifference;
    Map<Status, Long> averageStatusEvaluationDifference;
}
