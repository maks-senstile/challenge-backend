package org.example.domain.eventbus;

public interface EventBus {

    void publish(AbstractEvent event);

    void addEventListener(EventListener<Object> listener);
}
