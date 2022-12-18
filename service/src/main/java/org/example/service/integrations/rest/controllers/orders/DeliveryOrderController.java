package org.example.service.integrations.rest.controllers.orders;

import lombok.RequiredArgsConstructor;
import org.example.domain.model.orders.DeliveryOrder;
import org.example.domain.services.orders.DeliveryOrderService;
import org.example.service.integrations.rest.controllers.orders.dto.CreateDeliveryOrderCommandDto;
import org.example.service.integrations.rest.controllers.orders.dto.OrderResponse;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class DeliveryOrderController {

    private final DeliveryOrderService service;

    @GetMapping("/orders")
    public List<OrderResponse> findAll(@RequestParam("offset") Long offset, @RequestParam("count") Integer count) {
        return service.getAllOrders(offset, count).stream()
                .map(OrderResponse::create)
                .collect(Collectors.toList());
    }

    @PostMapping("/orders/create")
    public OrderResponse create(@Valid @RequestBody CreateDeliveryOrderCommandDto createDto) {
        DeliveryOrder deliveryOrder = service.create(createDto);
        return OrderResponse.create(deliveryOrder);
    }
}
