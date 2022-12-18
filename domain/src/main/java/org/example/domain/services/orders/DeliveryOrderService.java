package org.example.domain.services.orders;

import org.example.domain.model.orders.DeliveryOrder;
import org.example.domain.services.orders.commands.CreateDeliveryOrderCommand;

import java.util.List;

public interface DeliveryOrderService {

    List<DeliveryOrder> getScheduledOrders(long offset, int count);

    List<DeliveryOrder> getAllOrders(long offset, int count);

    DeliveryOrder create(CreateDeliveryOrderCommand serviceEvent);

    void execute(DeliveryOrder deliveryOrder);
}
