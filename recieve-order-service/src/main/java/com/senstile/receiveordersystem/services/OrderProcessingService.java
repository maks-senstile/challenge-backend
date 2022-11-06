package com.senstile.receiveordersystem.services;

import com.senstile.receiveordersystem.exception.ProviderDeliveryException;
import com.senstile.receiveordersystem.model.Address;
import com.senstile.receiveordersystem.model.DeliveryOrder;
import com.senstile.receiveordersystem.model.DeliveryOrderScheduled;
import com.senstile.receiveordersystem.utils.Provider1Processor;
import com.senstile.receiveordersystem.utils.Provider2Processor;
import com.senstile.receiveordersystem.utils.Provider3Processor;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

import static com.senstile.receiveordersystem.utils.ConstantsUtils.PROVIDER_1;
import static com.senstile.receiveordersystem.utils.ConstantsUtils.PROVIDER_2;
import static com.senstile.receiveordersystem.utils.ConstantsUtils.PROVIDER_3;

@Component
@AllArgsConstructor
public class OrderProcessingService {

    private final Provider1Processor provider1Processor;
    private final Provider2Processor provider2Processor;
    private final Provider3Processor provider3Processor;

    public String sendToProcessing(DeliveryOrder deliveryOrder, Address address) throws ProviderDeliveryException {
        String providerName = detectProvider(address);
        deliveryOrder.setProviderName(providerName);
        return getRusult(providerName, address, deliveryOrder.getProductIds());
    }

    public String sendToProcessing(DeliveryOrderScheduled deliveryOrder, Address address) throws ProviderDeliveryException {
        String providerName = detectProvider(address);
        deliveryOrder.setProviderName(providerName);
        return getRusult(providerName, address, deliveryOrder.getProductIds());
    }

    private String getRusult(String providerName, Address address, List<Long> deliveryOrder) {
        String providerOrderId = null;
        if (providerName.equals(PROVIDER_1)) {
            providerOrderId = provider1Processor.process(address, deliveryOrder);
        } else if (providerName.equals(PROVIDER_2)) {
            providerOrderId = provider2Processor.process(address, deliveryOrder);
        } else if (providerName.equals(PROVIDER_3)) {
            providerOrderId = provider3Processor.process(address, deliveryOrder);
        }
        return providerOrderId;
    }

    private String detectProvider(Address address) {
        // emulate provider detection
        return List.of(PROVIDER_1, PROVIDER_2, PROVIDER_3).get(new Random().nextInt(3));
    }
}
