package org.reactive_java.data.collector.rx;


import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.BiConsumer;
import io.reactivex.rxjava3.functions.BiFunction;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.reactive_java.data.model.Evaluation;
import org.reactive_java.data.model.Task;
import org.reactive_java.data.model.User;

import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class ObservableCollector {
    private static BiConsumer<Map<User, Map<Task, Boolean>>, Task> accumulator() {
        return (map, task) -> {
            User user;
            user = task.getUser();
            Evaluation evaluation = task.getEvaluation();

            boolean taskCompletedOnTime = true;
//                    task.getStatuses()
//                    .stream()
//                    .noneMatch(
//                            taskStatus -> Duration.between(taskStatus.startTime(), taskStatus.finishTime())
//                                    .compareTo(evaluation.getStatusDurationMap().get(taskStatus.status())) > 0
//                    );

            map.computeIfAbsent(user, _ -> new HashMap<>()).put(task, taskCompletedOnTime);
        };
    }

    private static BiFunction<ConcurrentHashMap<User, Map<Task, Boolean>>, ConcurrentHashMap<User, Map<Task, Boolean>>, ConcurrentHashMap<User, Map<Task, Boolean>>> combiner() {
        return (map1, map2) -> {
            map2.forEach((user, value) -> map1.merge(user, value, (v1, v2) -> {
                v1.putAll(v2);
                return v1;
            }));
            return map1;
        };
    }

    private static Function<Map<User, Map<Task, Boolean>>, Map<User, Map<Task, Boolean>>> finisher() {
        return Collections::unmodifiableMap;
    }

    public static Observable<Map<User, Map<Task, Boolean>>> processTasks(List<Task> tasks, boolean useDelays) {
        Observable<Task> observable;

        if (useDelays) {
            observable = Observable.zip(Observable.fromIterable(tasks), Observable.interval(100, TimeUnit.NANOSECONDS),
                    (i, _) -> i);
        } else {
            observable = Observable.fromIterable(tasks);
        }

        return observable
                .observeOn(Schedulers.computation())
                .groupBy(Task::getUser)
                .flatMapSingle(groupedObservable ->
                        groupedObservable
                                .collect(ConcurrentHashMap::new, accumulator())
                )
                .reduce(combiner())
                .map(finisher())
                .toObservable();
    }
}
