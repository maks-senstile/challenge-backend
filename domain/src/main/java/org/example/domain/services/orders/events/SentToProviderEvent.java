package org.example.domain.services.orders.events;

import lombok.Getter;
import org.example.domain.eventbus.AbstractEvent;
import org.example.domain.model.orders.DeliveryOrder;

public class SentToProviderEvent extends AbstractEvent {

    public static String NAME = "SentToProviderEvent";

    @Getter
    private final Long orderId;

    private SentToProviderEvent(Long orderId) {
        super(NAME);
        this.orderId = orderId;
    }

    public static SentToProviderEvent createFromOrder(final DeliveryOrder order) {
        return new SentToProviderEvent(order.getId());
    }
}
