package org.example.api.integrations.rest.controllers.orders.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.example.domain.model.orders.DeliveryOrder;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DeliveryOrderResponse {

    public static DeliveryOrderResponse create(DeliveryOrder order) {
        return new DeliveryOrderResponse();
    }
}
