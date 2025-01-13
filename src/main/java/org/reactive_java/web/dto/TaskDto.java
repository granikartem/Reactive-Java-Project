package org.reactive_java.web.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.reactive_java.data.model.Evaluation;
import org.reactive_java.data.model.TaskPriority;
import org.reactive_java.data.model.TaskStatus;
import org.reactive_java.data.model.User;

import java.time.Duration;
import java.util.List;

@Data
@JsonSerialize
@JsonDeserialize
public class TaskDto {
    TaskPriority priority;
    Duration completionTime;
    Duration evaluation;
    String userName;
    String description;
}
