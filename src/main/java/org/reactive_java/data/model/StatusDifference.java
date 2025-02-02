package org.reactive_java.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Duration;

@Data
@AllArgsConstructor
public class StatusDifference {
    boolean isFaster;

    Duration difference;
}
