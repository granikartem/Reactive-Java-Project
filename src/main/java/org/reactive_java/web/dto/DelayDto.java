package org.reactive_java.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.beans.ConstructorProperties;

@Data
@JsonSerialize
@JsonDeserialize
public class DelayDto {
    public long delay;

    @ConstructorProperties("delay")
    public DelayDto(@JsonProperty(value = "delay") long delay) {
        this.delay = delay;
    }
}
