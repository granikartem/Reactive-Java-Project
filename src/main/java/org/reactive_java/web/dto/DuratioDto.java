package org.reactive_java.web.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.beans.ConstructorProperties;
import java.time.Duration;

@Data
@JsonSerialize
@JsonDeserialize
public class DuratioDto {
    private Duration duration;

    @ConstructorProperties({"duration"})
    public DuratioDto(Duration duration) {
        this.duration = duration;
    }

    public Duration toDuration() {
        return duration;
    }
}
