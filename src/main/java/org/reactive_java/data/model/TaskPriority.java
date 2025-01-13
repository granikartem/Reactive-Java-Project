package org.reactive_java.data.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TaskPriority {

    MAJOR("MAJOR"),

    MINOR("MINOR"),

    TRIVIAL("TRIVIAL"),

    CRITICAL("CRITICAL");

    private final String code;
}
