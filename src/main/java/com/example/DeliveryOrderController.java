package com.example;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Api
@RestController
public class DeliveryOrderController {

    @Autowired
    private ApplicationContext context;
    @Autowired
    private UserController userController;

    @PostMapping("/create-new-delivery-order")
    public ResponseEntity create(HttpServletRequest request) {
        String userId = request.getParameter("userId");
        String addressId = request.getParameter("addressId");
        String executeAt = request.getParameter("executeAt");
        String productIds = request.getParameter("productIds");
        if (userId == null || addressId == null || productIds == null || executeAt == null) {
            return new ResponseEntity("Required params are missing", HttpStatus.BAD_REQUEST);
        }
        try {
            userController.findById(Long.parseLong(userId));
        } catch (Exception e) {
            return new ResponseEntity("User not found", HttpStatus.NOT_FOUND);
        }
        if (!context.getBean(AddressRepository.class).findById(Long.parseLong(addressId)).isPresent()) {
            return new ResponseEntity("Payment method not found", HttpStatus.NOT_FOUND);
        }

        DeliveryOrderService deliveryOrderService = context.getBean(DeliveryOrderService.class);
        Object body;
        if (executeAt.equals("ASAP")) {
            DeliveryOrder deliveryOrder = new DeliveryOrder();
            deliveryOrder.setUserId(Long.parseLong(userId));
            deliveryOrder.setAddressId(Long.parseLong(addressId));
            deliveryOrder.setProductIds(Arrays.stream(productIds.split(",")).map(id -> Long.parseLong(id)).collect(Collectors.toList()));
            deliveryOrder.setCreatedAt(Instant.now());
            deliveryOrder.setStatus(OrderStatus.PENDING);
            deliveryOrderService.create(deliveryOrder);
            body = deliveryOrder;
        } else {
            DeliveryOrderScheduled deliveryOrderScheduled = new DeliveryOrderScheduled();
            deliveryOrderScheduled.setUserId(Long.parseLong(userId));
            deliveryOrderScheduled.setAddressId(Long.parseLong(addressId));
            deliveryOrderScheduled.setProductIds(Arrays.stream(productIds.split(",")).map(id -> Long.parseLong(id)).collect(Collectors.toList()));
            deliveryOrderScheduled.setCreatedAt(Instant.now());
            deliveryOrderScheduled.setExecuteAt(Instant.parse(executeAt));
            deliveryOrderScheduled.setStatus(OrderStatus.PENDING);
            deliveryOrderService.schedule(deliveryOrderScheduled);
            body = deliveryOrderScheduled;
        }

        return new ResponseEntity(body, HttpStatus.OK);
    }

    @GetMapping("/find-all-delivery-orders")
    public ResponseEntity findAll() {
        List<DeliveryOrder> deliveryOrders = context.getBean(DeliveryOrderRepository.class).findAll();
        List<DeliveryOrderScheduled> ordersScheduled = context.getBean(DeliveryOrderScheduledRepository.class).findAll();
        List<Object> result = new ArrayList<>();
        result.addAll(deliveryOrders);
        result.addAll(ordersScheduled);

        return new ResponseEntity(result, HttpStatus.OK);
    }
}
