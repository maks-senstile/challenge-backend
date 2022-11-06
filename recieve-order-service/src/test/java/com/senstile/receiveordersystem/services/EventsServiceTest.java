package com.senstile.receiveordersystem.services;

import com.senstile.receiveordersystem.enums.OrderStatus;
import com.senstile.receiveordersystem.model.DeliveryOrder;
import com.senstile.receiveordersystem.model.DeliveryOrderScheduled;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

class EventsServiceTest {

    private EventsService eventsServiceUnderTest;

    @BeforeEach
    void setUp() {
        eventsServiceUnderTest = new EventsService();
    }

    @Test
    void testCreated1() {
        // Setup
        final DeliveryOrder deliveryOrder = new DeliveryOrder();
        deliveryOrder.setId(0L);
        deliveryOrder.setAddressId(0L);
        deliveryOrder.setUserId(0L);
        deliveryOrder.setProductIds(List.of(0L));
        deliveryOrder.setCreatedAt(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0).toInstant(ZoneOffset.UTC));
        deliveryOrder.setStatus(OrderStatus.PENDING);
        deliveryOrder.setProviderName("providerName");
        deliveryOrder.setProviderOrderId("providerOrderId");

        // Run the test
        eventsServiceUnderTest.created(deliveryOrder);

        // Verify the results
    }

    @Test
    void testCreated2() {
        // Setup
        final DeliveryOrderScheduled deliveryOrderScheduled = new DeliveryOrderScheduled();
        deliveryOrderScheduled.setId(0L);
        deliveryOrderScheduled.setAddressId(0L);
        deliveryOrderScheduled.setUserId(0L);
        deliveryOrderScheduled.setProductIds(List.of(0L));
        deliveryOrderScheduled.setCreatedAt(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0).toInstant(ZoneOffset.UTC));
        deliveryOrderScheduled.setStatus(OrderStatus.PENDING);
        deliveryOrderScheduled.setProviderName("providerName");
        deliveryOrderScheduled.setProviderOrderId("providerOrderId");
        deliveryOrderScheduled.setExecuteAt(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0).toInstant(ZoneOffset.UTC));

        // Run the test
        eventsServiceUnderTest.created(deliveryOrderScheduled);

        // Verify the results
    }

    @Test
    void testSentToProvider1() {
        // Setup
        final DeliveryOrder deliveryOrder = new DeliveryOrder();
        deliveryOrder.setId(0L);
        deliveryOrder.setAddressId(0L);
        deliveryOrder.setUserId(0L);
        deliveryOrder.setProductIds(List.of(0L));
        deliveryOrder.setCreatedAt(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0).toInstant(ZoneOffset.UTC));
        deliveryOrder.setStatus(OrderStatus.PENDING);
        deliveryOrder.setProviderName("providerName");
        deliveryOrder.setProviderOrderId("providerOrderId");

        // Run the test
        eventsServiceUnderTest.sentToProvider(deliveryOrder);

        // Verify the results
    }

    @Test
    void testSentToProvider2() {
        // Setup
        final DeliveryOrderScheduled deliveryOrderScheduled = new DeliveryOrderScheduled();
        deliveryOrderScheduled.setId(0L);
        deliveryOrderScheduled.setAddressId(0L);
        deliveryOrderScheduled.setUserId(0L);
        deliveryOrderScheduled.setProductIds(List.of(0L));
        deliveryOrderScheduled.setCreatedAt(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0).toInstant(ZoneOffset.UTC));
        deliveryOrderScheduled.setStatus(OrderStatus.PENDING);
        deliveryOrderScheduled.setProviderName("providerName");
        deliveryOrderScheduled.setProviderOrderId("providerOrderId");
        deliveryOrderScheduled.setExecuteAt(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0).toInstant(ZoneOffset.UTC));

        // Run the test
        eventsServiceUnderTest.sentToProvider(deliveryOrderScheduled);

        // Verify the results
    }

    @Test
    void testFailedToSendToProvider1() {
        // Setup
        final DeliveryOrder deliveryOrder = new DeliveryOrder();
        deliveryOrder.setId(0L);
        deliveryOrder.setAddressId(0L);
        deliveryOrder.setUserId(0L);
        deliveryOrder.setProductIds(List.of(0L));
        deliveryOrder.setCreatedAt(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0).toInstant(ZoneOffset.UTC));
        deliveryOrder.setStatus(OrderStatus.PENDING);
        deliveryOrder.setProviderName("providerName");
        deliveryOrder.setProviderOrderId("providerOrderId");

        // Run the test
        eventsServiceUnderTest.failedToSendToProvider(deliveryOrder);

        // Verify the results
    }

    @Test
    void testFailedToSendToProvider2() {
        // Setup
        final DeliveryOrderScheduled deliveryOrderScheduled = new DeliveryOrderScheduled();
        deliveryOrderScheduled.setId(0L);
        deliveryOrderScheduled.setAddressId(0L);
        deliveryOrderScheduled.setUserId(0L);
        deliveryOrderScheduled.setProductIds(List.of(0L));
        deliveryOrderScheduled.setCreatedAt(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0).toInstant(ZoneOffset.UTC));
        deliveryOrderScheduled.setStatus(OrderStatus.PENDING);
        deliveryOrderScheduled.setProviderName("providerName");
        deliveryOrderScheduled.setProviderOrderId("providerOrderId");
        deliveryOrderScheduled.setExecuteAt(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0).toInstant(ZoneOffset.UTC));

        // Run the test
        eventsServiceUnderTest.failedToSendToProvider(deliveryOrderScheduled);

        // Verify the results
    }
}
