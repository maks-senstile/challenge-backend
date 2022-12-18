package org.example.service.configs;

import org.example.domain.listeners.FailedToSendToProviderEventListener;
import org.example.domain.listeners.OrderCreatedEventListener;
import org.example.domain.listeners.SentToProviderEventListener;
import org.example.domain.model.orders.DeliveryOrderRepository;
import org.example.domain.services.ExternalEventService;
import org.example.domain.services.orders.DeliveryOrderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ListenersConfig {

    @Bean
    public FailedToSendToProviderEventListener failedToSendToProviderEventListener(
            DeliveryOrderRepository deliveryOrderRepository,
            ExternalEventService externalEventService
    ) {
        return new FailedToSendToProviderEventListener(deliveryOrderRepository, externalEventService);
    }

    @Bean
    public OrderCreatedEventListener orderCreatedEventListener(
            DeliveryOrderRepository deliveryOrderRepository,
            DeliveryOrderService deliveryOrderService,
            ExternalEventService externalEventService
    ) {
        return new OrderCreatedEventListener(deliveryOrderRepository, deliveryOrderService, externalEventService);
    }

    @Bean
    public SentToProviderEventListener sentToProviderEventListener(
            DeliveryOrderRepository deliveryOrderRepository,
            ExternalEventService externalEventService
    ) {
        return new SentToProviderEventListener(deliveryOrderRepository, externalEventService);
    }
}
