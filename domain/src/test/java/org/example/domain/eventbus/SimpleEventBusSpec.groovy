package org.example.domain.eventbus

import spock.lang.Specification

import java.util.concurrent.Executor

class SimpleEventBusSpec extends Specification {

    class TestEvent extends AbstractEvent {

        public static final String NAME = "TestEvent"

        public TestEvent() {
            super(NAME)
        }
    }

    def "Scenario: Bus has to add listeners and delegate events"() {
        given: "The bus"
        Executor executor = (Runnable r) -> r.run()
        def eventBus = new SimpleEventBus(executor)
        def listener = Mock(EventListener.class)
        listener.eventName >> TestEvent.NAME
        eventBus.addEventListener(listener)
        when:
        eventBus.publish(new TestEvent())
        then:
        1 * listener.onEvent(_)
    }
}
