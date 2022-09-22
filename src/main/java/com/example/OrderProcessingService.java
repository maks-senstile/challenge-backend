package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class OrderProcessingService {

    @Autowired
    private Provider1Processor provider1Processor;
    @Autowired
    private Provider2Processor provider2Processor;
    @Autowired
    private Provider3Processor provider3Processor;

    public String sendToProcessing(DeliveryOrder deliveryOrder, Address address) throws ProviderDeliveryException {
        String providerName = detectProvider(address);
        deliveryOrder.setProviderName(providerName);
        String providerOrderId = null;
        if (providerName.equals("PROVIDER_1")) {
            providerOrderId = provider1Processor.process(address, deliveryOrder.getProductIds());
        } else if (providerName.equals("PROVIDER_2")) {
            providerOrderId = provider2Processor.process(address, deliveryOrder.getProductIds());
        } else if (providerName.equals("PROVIDER_3")) {
            providerOrderId = provider3Processor.process(address, deliveryOrder.getProductIds());
        }
        return providerOrderId;
    }

    public String sendToProcessing(DeliveryOrderScheduled deliveryOrder, Address address) throws ProviderDeliveryException {
        String providerName = detectProvider(address);
        deliveryOrder.setProviderName(providerName);
        String providerOrderId = null;
        if (providerName.equals("PROVIDER_1")) {
            providerOrderId = provider1Processor.process(address, deliveryOrder.getProductIds());
        } else if (providerName.equals("PROVIDER_2")) {
            providerOrderId = provider2Processor.process(address, deliveryOrder.getProductIds());
        } else if (providerName.equals("PROVIDER_3")) {
            providerOrderId = provider3Processor.process(address, deliveryOrder.getProductIds());
        }
        return providerOrderId;
    }

    private String detectProvider(Address address) {
        // emulate provider detection
        return List.of("PROVIDER_1", "PROVIDER_2", "PROVIDER_3").get(new Random().nextInt(3));
    }
}
