package org.reactive_java.web.service;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.processors.PublishProcessor;
import org.reactive_java.data.model.Task;
import org.reactive_java.data.model.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StatisticsService {
//    private int totalTaskCount = 0;
//    private Map<User, Integer> userTaskCount = new HashMap<>();
//    private PublishProcessor<Map<String, Object>> statsPublisher = PublishProcessor.create();
//
//    public void processTask(Task task) {
//        totalTaskCount++;
//        userTaskCount.merge(task.getUser(), 1, Integer::sum);
//        publishStats();
//    }
//
//    private void publishStats() {
//        Map<String, Object> stats = new HashMap<>();
//        stats.put("totalTasks", totalTaskCount);
//        stats.put("userStats", new HashMap<>(userTaskCount));
//        statsPublisher.onNext(stats);
//    }
//
//    public Flowable<Map<String, Object>> getStatsFlowable() {
//        return statsPublisher;
//    }
}
