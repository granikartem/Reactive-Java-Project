package org.reactive_java.web.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.reactive_java.data.model.Task;

import java.util.List;

@Data
@JsonSerialize
@AllArgsConstructor
public class CachedTasksDto {
    private List<Task> tasks;
}
