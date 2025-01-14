package org.reactive_java.data.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import java.util.Set;

@Builder
@RequiredArgsConstructor
@JsonSerialize
@Data
public class User {

    private final String login;

    private final Set<String> groups;
}
