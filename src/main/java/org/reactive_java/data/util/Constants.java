package org.reactive_java.data.util;

import org.reactive_java.data.model.Status;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public final class Constants {
    public final static Duration MAX_DURATION = Duration.ofHours(12);

    public final static int STATUS_AMOUNT = Status.values().length;

    public final static Map<Status, Double> STATUS_TIME_SHARE_MAP = Map.of(
            Status.WAITING, 0.0625,
            Status.IN_PROGRESS, 0.5375,
            Status.TESTING, 0.3375,
            Status.INTEGRATION, 0.0625
    );

    public final static int USER_AMOUNT = 1000;
    public final static int MAX_GROUPS_PER_USER = 4;

    public final static List<String> GROUP_NAMES = List.of(
            "ADMIN",
            "DEVELOPER",
            "DEVOPS",
            "PRODUCT",
            "TESTER",
            "SUPPORT",
            "DESIGNER",
            "DOCUMENTATION",
            "OTHER"
    );

    public final static List<String> USER_NAMES = List.of(
            "Ivan",
            "Sergey",
            "Peter",
            "Vladimir",
            "Artem",
            "Pavel",
            "Egor",
            "Igor",
            "Anton",
            "Vasiliy",
            "Ilya",
            "Gennadiy",
            "Mark",
            "Daniil",
            "Vyacheslav"
    );

    public final static List<String> USER_SURNAMES = List.of(
            "Ivanov",
            "Sergeyev",
            "Petrov",
            "Kuznetsov",
            "Sidorov",
            "Pavlov",
            "Isayev",
            "Bukin",
            "Nikolayev",
            "Markov",
            "Klimenkov"
    );
}
