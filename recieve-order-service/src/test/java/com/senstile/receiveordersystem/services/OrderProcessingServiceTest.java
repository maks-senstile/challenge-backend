package com.senstile.receiveordersystem.services;

import com.senstile.receiveordersystem.enums.OrderStatus;
import com.senstile.receiveordersystem.exception.ProviderDeliveryException;
import com.senstile.receiveordersystem.model.Address;
import com.senstile.receiveordersystem.model.DeliveryOrder;
import com.senstile.receiveordersystem.model.DeliveryOrderScheduled;
import com.senstile.receiveordersystem.model.User;
import com.senstile.receiveordersystem.utils.Provider1Processor;
import com.senstile.receiveordersystem.utils.Provider2Processor;
import com.senstile.receiveordersystem.utils.Provider3Processor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class OrderProcessingServiceTest {

    @Mock
    private Provider1Processor mockProvider1Processor;
    @Mock
    private Provider2Processor mockProvider2Processor;
    @Mock
    private Provider3Processor mockProvider3Processor;

    private OrderProcessingService orderProcessingServiceUnderTest;

    @BeforeEach
    void setUp() {
        orderProcessingServiceUnderTest = new OrderProcessingService(mockProvider1Processor, mockProvider2Processor, mockProvider3Processor);
    }

    @Test
    void testSendToProcessing1() {
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

        final Address address = new Address();
        address.setId(0L);
        final User user = new User();
        user.setId(0L);
        user.setFirstName("firstName");
        user.setAddresses(List.of(new Address()));
        address.setUser(user);
        address.setAddressLine("addressLine");
        address.setCity("city");
        address.setCountry("country");
        address.setPostalCode("postalCode");

        when(mockProvider1Processor.process(any(), any())).thenReturn("result");
        when(mockProvider2Processor.process(any(), any())).thenReturn("result");
        when(mockProvider3Processor.process(any(), any())).thenReturn("result");

        // Run the test
        String result = orderProcessingServiceUnderTest.sendToProcessing(deliveryOrder, address);

        // Verify the results
        assertThat(result).isEqualTo("result");
    }

    @Test
    void testSendToProcessing2() {
        // Setup
        final DeliveryOrderScheduled deliveryOrder = new DeliveryOrderScheduled();
        deliveryOrder.setId(0L);
        deliveryOrder.setAddressId(0L);
        deliveryOrder.setUserId(0L);
        deliveryOrder.setProductIds(List.of(0L));
        deliveryOrder.setCreatedAt(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0).toInstant(ZoneOffset.UTC));
        deliveryOrder.setStatus(OrderStatus.PENDING);
        deliveryOrder.setProviderName("providerName");
        deliveryOrder.setProviderOrderId("providerOrderId");
        deliveryOrder.setExecuteAt(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0).toInstant(ZoneOffset.UTC));

        final Address address = new Address();
        address.setId(0L);
        final User user = new User();
        user.setId(0L);
        user.setFirstName("firstName");
        user.setAddresses(List.of(new Address()));
        address.setUser(user);
        address.setAddressLine("addressLine");
        address.setCity("city");
        address.setCountry("country");
        address.setPostalCode("postalCode");

        when(mockProvider1Processor.process(any(), any())).thenReturn("result");
        when(mockProvider2Processor.process(any(), any())).thenReturn("result");
        when(mockProvider3Processor.process(any(), any())).thenReturn("result");

        // Run the test
        final String result = orderProcessingServiceUnderTest.sendToProcessing(deliveryOrder, address);

        // Verify the results
        assertThat(result).isEqualTo("result");
    }

}
