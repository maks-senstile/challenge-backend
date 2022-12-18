package org.example.domain.eventbus;

public interface EventListener<E> {

    String getEventName();

    void onEvent(E event);
}
