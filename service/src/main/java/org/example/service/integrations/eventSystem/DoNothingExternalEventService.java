package org.example.service.integrations.eventSystem;

import org.example.domain.model.orders.DeliveryOrder;
import org.example.domain.services.ExternalEventService;

public class DoNothingExternalEventService implements ExternalEventService {
    @Override
    public void created(DeliveryOrder deliveryOrder) {
        
    }

    @Override
    public void sentToProvider(DeliveryOrder deliveryOrder) {

    }

    @Override
    public void failedToSendToProvider(DeliveryOrder deliveryOrder) {

    }
}
