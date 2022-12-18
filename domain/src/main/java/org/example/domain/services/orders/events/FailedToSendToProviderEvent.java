package org.example.domain.services.orders.events;

import lombok.Getter;
import org.example.domain.eventbus.AbstractEvent;
import org.example.domain.model.orders.DeliveryOrder;

public class FailedToSendToProviderEvent extends AbstractEvent {

    public static String NAME = "FailedToSendToProviderEvent";

    @Getter
    private final Long orderId;

    private FailedToSendToProviderEvent(Long orderId) {
        super(NAME);
        this.orderId = orderId;
    }

    public static FailedToSendToProviderEvent createFromOrder(final DeliveryOrder order) {
        return new FailedToSendToProviderEvent(order.getId());
    }
}
