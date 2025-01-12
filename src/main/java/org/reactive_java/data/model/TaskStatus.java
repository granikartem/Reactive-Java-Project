package org.reactive_java.data.model;

import lombok.Builder;

import java.time.Duration;

@Builder
public record TaskStatus(Status status, Duration completionTime) {
}
