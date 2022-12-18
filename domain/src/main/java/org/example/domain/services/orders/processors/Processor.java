package org.example.domain.services.orders.processors;

import org.example.domain.model.addresses.Address;
import org.example.domain.model.orders.DeliveryOrder;

public interface Processor {
    boolean canAccept(Address address);

    void process(DeliveryOrder deliveryOrder);
}
