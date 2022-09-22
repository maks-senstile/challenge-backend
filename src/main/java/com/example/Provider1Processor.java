package com.example;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class Provider1Processor {

    public String process(Address address, List<Long> productIds) {
        // Here is the logic to send an order to a specific provider for delivering
        // Assume that it returns an internal order id in case a provider accepts an order otherwise throws ProviderDeliveryException
        return UUID.randomUUID().toString();
    }
}
