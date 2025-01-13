package org.reactive_java.web.dto;

import java.time.Duration;

public class ManipulationDelayDto {
    public Duration toDuration() {
        return Duration.ofHours(1);
    }
}
