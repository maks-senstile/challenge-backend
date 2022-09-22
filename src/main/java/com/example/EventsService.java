package com.example;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EventsService {

    @Async
    public void created(DeliveryOrder deliveryOrder) {
        // build and send an event in message queue async
    }

    @Async
    public void created(DeliveryOrderScheduled deliveryOrderScheduled) {
        // build and send an event in message queue async
    }

    @Async
    public void sentToProvider(DeliveryOrder deliveryOrder) {
        // build and send an event in message queue async
    }

    @Async
    public void sentToProvider(DeliveryOrderScheduled deliveryOrderScheduled) {
        // build and send an event in message queue async
    }

    @Async
    public void failedToSendToProvider(DeliveryOrder deliveryOrder) {
        // build and send an event in message queue async
    }

    @Async
    public void failedToSendToProvider(DeliveryOrderScheduled deliveryOrderScheduled) {
        // build and send an event in message queue async
    }
}
