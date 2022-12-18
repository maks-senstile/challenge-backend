package org.example.domain.eventbus;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

public class SimpleEventBus implements EventBus {

    private final ConcurrentHashMap<String, Set<EventListener<Object>>> listenersMap = new ConcurrentHashMap<>();
    private final Executor executor;

    public SimpleEventBus(Executor executor) {
        this.executor = executor;
    }

    @Override
    public void publish(AbstractEvent event) {
        executor.execute(() -> {
            Set<EventListener<Object>> listeners = listenersMap.getOrDefault(event.getName(), Collections.emptySet());
            listeners.forEach(listener -> listener.onEvent(event));
        });
    }

    @Override
    synchronized public void addEventListener(EventListener<Object> listener) {
        listenersMap.putIfAbsent(listener.getEventName(), new HashSet<>());
        listenersMap.get(listener.getEventName()).add(listener);
    }
}
