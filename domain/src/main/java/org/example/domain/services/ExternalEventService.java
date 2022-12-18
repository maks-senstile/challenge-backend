package org.example.domain.services;

import org.example.domain.model.orders.DeliveryOrder;

public interface ExternalEventService {

    /**
     * build and send an event in message queue async
     * @param deliveryOrder
     */
    void created(final DeliveryOrder deliveryOrder);

    /**
     * build and send an event in message queue async
     * @param deliveryOrder
     */
    void sentToProvider(final DeliveryOrder deliveryOrder);

    /**
     * build and send an event in message queue async
     * @param deliveryOrder
     */
    void failedToSendToProvider(final DeliveryOrder deliveryOrder);
}
