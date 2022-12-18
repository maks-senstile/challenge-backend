package org.example.domain.services.orders.processors.implementations;

import org.example.domain.eventbus.EventBus;
import org.example.domain.model.addresses.Address;
import org.example.domain.services.orders.processors.AbstractProcessor;

import java.util.List;
import java.util.UUID;

public class Provider1Processor extends AbstractProcessor {

    private static final String NAME = "PROVIDER_1";

    public Provider1Processor(EventBus eventBus) {
        super(NAME, eventBus);
    }

    @Override
    public boolean canAccept(Address address) {
        return true;
    }

    @Override
    protected String doProcess(Address address, List<Long> productIds) {
        // Here is the logic to send an order to a specific provider for delivering
        // Assume that it returns an internal order id in case a provider accepts an order otherwise throws ProviderDeliveryException
        return UUID.randomUUID().toString();
    }
}
