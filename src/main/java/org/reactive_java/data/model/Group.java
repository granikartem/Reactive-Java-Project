package org.reactive_java.data.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Data
@RequiredArgsConstructor
public class Group {
    String name;

    Set<User> members;
}
