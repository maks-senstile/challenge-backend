package org.example.domain.listeners;

import lombok.RequiredArgsConstructor;
import org.example.domain.eventbus.EventListener;
import org.example.domain.exceptions.EntityNotFoundDomainException;
import org.example.domain.model.orders.DeliveryOrder;
import org.example.domain.model.orders.DeliveryOrderRepository;
import org.example.domain.services.ExternalEventService;
import org.example.domain.services.orders.events.SentToProviderEvent;

@RequiredArgsConstructor
public class SentToProviderEventListener implements EventListener<SentToProviderEvent> {

    private final DeliveryOrderRepository repository;
    private final ExternalEventService externalEventService;
    @Override
    public String getEventName() {
        return SentToProviderEvent.NAME;
    }

    @Override
    public void onEvent(SentToProviderEvent event) {
        DeliveryOrder deliveryOrder = repository.findById(event.getOrderId())
                .orElseThrow(EntityNotFoundDomainException::new);
        externalEventService.sentToProvider(deliveryOrder);
    }
}
