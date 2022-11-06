package com.senstile.receiveordersystem.converter;

import com.senstile.receiveordersystem.dto.CommonDeliveryOrderDto;
import com.senstile.receiveordersystem.enums.OrderStatus;
import com.senstile.receiveordersystem.model.DeliveryOrderScheduled;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DeliveryOrderScheduledConverterTest {

    private DeliveryOrderScheduledConverter deliveryOrderScheduledConverterUnderTest;

    @BeforeEach
    void setUp() {
        deliveryOrderScheduledConverterUnderTest = new DeliveryOrderScheduledConverter();
    }

    @Test
    void testConvert() {
        // Setup
        final DeliveryOrderScheduled source = new DeliveryOrderScheduled();
        source.setId(0L);
        source.setAddressId(0L);
        source.setUserId(0L);
        source.setProductIds(List.of(0L));
        source.setCreatedAt(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0).toInstant(ZoneOffset.UTC));
        source.setStatus(OrderStatus.PENDING);
        source.setProviderName("providerName");
        source.setProviderOrderId("providerOrderId");
        source.setExecuteAt(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0).toInstant(ZoneOffset.UTC));

        final CommonDeliveryOrderDto expectedResult = CommonDeliveryOrderDto.builder()
                .id(0L)
                .addressId(0L)
                .userId(0L)
                .productIds(List.of(0L))
                .createdAt(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0).toInstant(ZoneOffset.UTC))
                .status(OrderStatus.PENDING)
                .providerName("providerName")
                .providerOrderId("providerOrderId")
                .executeAt(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0).toInstant(ZoneOffset.UTC))
                .build();

        // Run the test
        final CommonDeliveryOrderDto result = deliveryOrderScheduledConverterUnderTest.convert(source);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }
}
