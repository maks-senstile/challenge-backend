package com.senstile.receiveordersystem.converter;

import com.senstile.receiveordersystem.dto.CommonDeliveryOrderDto;
import com.senstile.receiveordersystem.enums.OrderStatus;
import com.senstile.receiveordersystem.model.DeliveryOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DeliveryOrderConverterTest {

    private DeliveryOrderConverter deliveryOrderConverterUnderTest;

    @BeforeEach
    void setUp() {
        deliveryOrderConverterUnderTest = new DeliveryOrderConverter();
    }

    @Test
    void testConvert() {
        // Setup
        final DeliveryOrder source = new DeliveryOrder();
        source.setId(0L);
        source.setAddressId(0L);
        source.setUserId(0L);
        source.setProductIds(List.of(0L));
        source.setCreatedAt(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0).toInstant(ZoneOffset.UTC));
        source.setStatus(OrderStatus.PENDING);
        source.setProviderName("providerName");
        source.setProviderOrderId("providerOrderId");

        final CommonDeliveryOrderDto expectedResult = CommonDeliveryOrderDto.builder()
                .id(0L)
                .productIds(List.of(0L))
                .createdAt(LocalDateTime.of(2020, 1, 1, 0, 0, 0, 0).toInstant(ZoneOffset.UTC))
                .status(OrderStatus.PENDING)
                .addressId(0L)
                .userId(0L)
                .providerName("providerName")
                .providerOrderId("providerOrderId")
                .build();

        // Run the test
        final CommonDeliveryOrderDto result = deliveryOrderConverterUnderTest.convert(source);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }
}
