package org.example.service.integrations.rest.controllers.orders.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.domain.model.orders.DeliveryOrder;
import org.example.domain.model.orders.embedded.Schedule;
import org.example.domain.model.orders.enums.OrderStatus;

import java.time.Instant;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderResponse {

    private final Long id;

    private final Long addressId;

    private final Long userId;

    private final List<Long> productIds;

    private final Instant createdAt;

    private final String processor;

    private final String providerOrderId;

    @JsonProperty("executeAt")
    private final Schedule schedule;

    private final OrderStatus status;

    public static OrderResponse create(DeliveryOrder order) {
        return new OrderResponse(
                order.getId(),
                order.getAddress().getId(),
                order.getUserId(),
                order.getProductIds(),
                order.getCreatedAt(),
                order.getProcessor(),
                order.getProviderOrderId(),
                order.getSchedule(),
                order.getStatus()
        );
    }
}
