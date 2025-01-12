package org.reactive_java.web.controller;

import io.reactivex.Flowable;
import org.reactive_java.data.generator.TaskGenerator;
import org.reactive_java.data.model.Task;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalTime;

@RestController
public class StatisticsController {
    @GetMapping("/stats/test")
    public void test() {
        System.out.println("------------------------------------>test");
    }

    @GetMapping(path = "/stats/test2", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamFlux() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(_ -> "Flux - " + LocalTime.now().toString());

    }

    @GetMapping(path = "/stats/test3", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flowable<String> streamFlowable() {
        var tasks = TaskGenerator.generateTasks(100);
        return Flowable.fromIterable(tasks).map(Task::toString).onBackpressureBuffer();
    }
}
