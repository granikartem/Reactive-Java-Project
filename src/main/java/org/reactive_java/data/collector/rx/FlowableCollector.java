package org.reactive_java.data.collector.rx;

import io.reactivex.rxjava3.core.Flowable;
import org.reactive_java.data.collector.rx.subscriber.TaskSubscriber;
import org.reactive_java.data.model.Task;
import org.reactive_java.data.model.User;

import java.util.List;
import java.util.Map;


public class FlowableCollector {
    public static Map<User, Map<Task, Boolean>> processTasks(List<Task> tasks) {
        Flowable<Task> tasksFlowable  = Flowable.fromIterable(tasks).onBackpressureBuffer();

        TaskSubscriber taskSubscriber = new TaskSubscriber();

        tasksFlowable.subscribe(taskSubscriber);
        return taskSubscriber.getStats();
    }
}
