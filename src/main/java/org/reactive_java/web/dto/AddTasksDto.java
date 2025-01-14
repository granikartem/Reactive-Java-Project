package org.reactive_java.web.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

@Data
@JsonSerialize
@JsonDeserialize
public class AddTasksDto {
    int amount;
    TaskDto taskSpecification;
}
