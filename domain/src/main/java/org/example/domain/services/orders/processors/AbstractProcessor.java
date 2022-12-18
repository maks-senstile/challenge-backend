package org.example.domain.services.orders.processors;


import lombok.RequiredArgsConstructor;
import org.example.domain.eventbus.EventBus;
import org.example.domain.exceptions.ProviderDeliveryDomainException;
import org.example.domain.model.addresses.Address;
import org.example.domain.model.orders.DeliveryOrder;
import org.example.domain.model.orders.enums.OrderStatusTrigger;
import org.example.domain.services.orders.events.FailedToSendToProviderEvent;
import org.example.domain.services.orders.events.SentToProviderEvent;

import java.util.List;

@RequiredArgsConstructor
public abstract class AbstractProcessor implements Processor {

    private final String processorName;
    private final EventBus eventBus;

    @Override
    public void process(DeliveryOrder deliveryOrder) {
        try {
            deliveryOrder.assignTo(processorName);
            deliveryOrder.fire(OrderStatusTrigger.START_EXECUTION);
            String processorOrderId = doProcess(deliveryOrder.getAddress(), deliveryOrder.getProductIds());
            deliveryOrder.setProviderOrderId(processorOrderId);
            eventBus.publish(SentToProviderEvent.createFromOrder(deliveryOrder));
        } catch (ProviderDeliveryDomainException e) {
            eventBus.publish(FailedToSendToProviderEvent.createFromOrder(deliveryOrder));
            deliveryOrder.fire(OrderStatusTrigger.ERROR_HAPPENED);
        } catch (Exception e) {
            deliveryOrder.fire(OrderStatusTrigger.UNKNOWN_ERROR_HAPPENED);
        }
    }
    protected abstract String doProcess(Address address, List<Long> productIds);
}
