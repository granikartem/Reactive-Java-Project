package org.reactive_java.data.model;

import lombok.RequiredArgsConstructor;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@RequiredArgsConstructor
@State(Scope.Benchmark)
public enum TaskPriority {

    MAJOR("MAJOR"),

    MINOR("MINOR"),

    TRIVIAL("TRIVIAL"),

    CRITICAL("CRITICAL");

    private final String code;
}
