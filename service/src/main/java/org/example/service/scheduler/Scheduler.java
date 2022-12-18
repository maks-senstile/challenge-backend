package org.example.service.scheduler;

import lombok.RequiredArgsConstructor;
import org.example.domain.model.orders.DeliveryOrder;
import org.example.domain.services.orders.DeliveryOrderService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "scheduler", name = "enabled", havingValue = "true")
public class Scheduler {

    private final static int INTERVAL_IN_MS = 5000;
    private final static int BATCH_SIZE = 100;

    private final DeliveryOrderService orderService;

    @Scheduled(fixedRate = Scheduler.INTERVAL_IN_MS)
    public void run() {
        List<DeliveryOrder> orders;
        do {
            orders = orderService.getScheduledOrders(0, Scheduler.BATCH_SIZE);
            orders.forEach(orderService::execute);
        } while (!orders.isEmpty());
    }
}
