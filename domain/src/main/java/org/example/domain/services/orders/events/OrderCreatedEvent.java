package org.example.domain.services.orders.events;

import lombok.Getter;
import org.example.domain.eventbus.AbstractEvent;
import org.example.domain.model.orders.DeliveryOrder;

public class OrderCreatedEvent extends AbstractEvent {

    public static String NAME = "OrderCreatedEvent";

    @Getter
    private final Long orderId;
    private OrderCreatedEvent(Long orderId) {
        super(NAME);
        this.orderId = orderId;
    }

    public static OrderCreatedEvent createFromOrder(final DeliveryOrder order) {
        return new OrderCreatedEvent(order.getId());
    }
}
