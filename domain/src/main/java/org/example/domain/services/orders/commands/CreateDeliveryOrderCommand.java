package org.example.domain.services.orders.commands;

import org.example.domain.model.orders.embedded.Schedule;

import java.util.List;

public interface CreateDeliveryOrderCommand {

    Long getUserId();

    Long getAddressId();

    Schedule getSchedule();

    List<Long> getProductIds();
}
