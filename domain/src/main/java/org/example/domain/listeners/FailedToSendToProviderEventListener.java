package org.example.domain.listeners;

import lombok.RequiredArgsConstructor;
import org.example.domain.eventbus.EventListener;
import org.example.domain.exceptions.EntityNotFoundDomainException;
import org.example.domain.model.orders.DeliveryOrder;
import org.example.domain.model.orders.DeliveryOrderRepository;
import org.example.domain.services.ExternalEventService;
import org.example.domain.services.orders.events.FailedToSendToProviderEvent;

@RequiredArgsConstructor
public class FailedToSendToProviderEventListener implements EventListener<FailedToSendToProviderEvent> {

    private final DeliveryOrderRepository repository;
    private final ExternalEventService externalEventService;
    @Override
    public String getEventName() {
        return FailedToSendToProviderEvent.NAME;
    }

    @Override
    public void onEvent(FailedToSendToProviderEvent event) {
        DeliveryOrder deliveryOrder = repository.findById(event.getOrderId())
                .orElseThrow(EntityNotFoundDomainException::new);
        externalEventService.failedToSendToProvider(deliveryOrder);
    }
}
