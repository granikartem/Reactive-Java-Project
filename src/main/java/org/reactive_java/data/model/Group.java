package org.reactive_java.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class Group {
    String name;

    Set<User> members;
}
