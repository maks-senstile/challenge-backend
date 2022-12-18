package org.example.api.integrations.rest.controllers.orders;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.example.api.integrations.rest.controllers.orders.dto.CreateDeliveryOrderCommandDto;
import org.example.api.integrations.rest.controllers.orders.dto.DeliveryOrderResponse;
import org.example.domain.services.orders.DeliveryOrderService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api
@RestController
@RequiredArgsConstructor
public class DeliveryOrderController {

    private final DeliveryOrderService service;

    @GetMapping("/orders")
    public List<DeliveryOrderResponse> getAllOrders(@RequestParam("offset") long offset, @RequestParam("count") int count) {
        return service.getAllOrders(offset, count).stream()
                .map(DeliveryOrderResponse::create)
                .collect(Collectors.toList());
    }

    @PostMapping("/orders/create")
    public DeliveryOrderResponse create(@Valid @RequestBody CreateDeliveryOrderCommandDto dto) {
        return DeliveryOrderResponse.create(service.create(dto));
    }
}
