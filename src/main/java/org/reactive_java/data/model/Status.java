package org.reactive_java.data.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Status {

    WAITING("WAITING", "В ожидании", false),

    IN_PROGRESS("IN_PROGRESS", "В работе", false),

    TESTING("TESTING", "Тестирование", false),

    INTEGRATION("INTEGRATION", "Интеграция", true);

    private final String code;

    private final String name;

    private final boolean finalStatus;
}
