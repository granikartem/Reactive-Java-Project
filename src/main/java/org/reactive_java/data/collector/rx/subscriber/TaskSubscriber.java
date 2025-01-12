package org.reactive_java.data.collector.rx.subscriber;

import io.reactivex.FlowableSubscriber;
import io.reactivex.annotations.NonNull;
import lombok.Getter;
import org.reactive_java.data.model.Evaluation;
import org.reactive_java.data.model.Task;
import org.reactive_java.data.model.User;
import org.reactivestreams.Subscription;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class TaskSubscriber implements FlowableSubscriber<Task> {
    private Map<User, Map<Task, Boolean>> stats;
    private Subscription subscription;

    @Override
    public void onSubscribe(@NonNull Subscription s) {
        stats = new ConcurrentHashMap<>();
        subscription = s;
        subscription.request(1);
    }

    @Override
    public void onNext(Task task) {
        User user;
        user = task.getUser();
        Evaluation evaluation = task.getEvaluation();

        boolean taskCompletedOnTime = task.getStatuses()
                .stream()
                .noneMatch(
                        taskStatus -> Duration.between(taskStatus.startTime(), taskStatus.finishTime())
                                .compareTo(evaluation.getStatusDurationMap().get(taskStatus.status())) > 0
                );

        stats.computeIfAbsent(user, _ -> new HashMap<>()).put(task, taskCompletedOnTime);
        subscription.request(1);
    }

    @Override
    public void onError(Throwable t) {
    }

    @Override
    public void onComplete() {
        stats = Collections.unmodifiableMap(stats);
        subscription.cancel();
    }
}
