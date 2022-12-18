package org.example.domain.listeners;

import lombok.RequiredArgsConstructor;
import org.example.domain.eventbus.EventListener;
import org.example.domain.exceptions.EntityNotFoundDomainException;
import org.example.domain.model.orders.DeliveryOrder;
import org.example.domain.model.orders.DeliveryOrderRepository;
import org.example.domain.services.ExternalEventService;
import org.example.domain.services.orders.DeliveryOrderService;
import org.example.domain.services.orders.events.OrderCreatedEvent;

@RequiredArgsConstructor
public class OrderCreatedEventListener implements EventListener<OrderCreatedEvent> {

    private final DeliveryOrderRepository repository;
    private final DeliveryOrderService deliveryOrderService;

    private final ExternalEventService externalEventService;

    @Override
    public String getEventName() {
        return OrderCreatedEvent.NAME;
    }

    @Override
    public void onEvent(OrderCreatedEvent event) {
        DeliveryOrder deliveryOrder = repository.findById(event.getOrderId())
                .orElseThrow(EntityNotFoundDomainException::new);
        externalEventService.created(deliveryOrder);
        if (deliveryOrder.getSchedule().isAsapExecutionRequired()) {
            deliveryOrderService.execute(deliveryOrder);
        }
    }
}
