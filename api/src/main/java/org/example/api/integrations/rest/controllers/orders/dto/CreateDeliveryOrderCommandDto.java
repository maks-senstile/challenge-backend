package org.example.api.integrations.rest.controllers.orders.dto;

import lombok.Getter;
import org.example.domain.model.orders.embedded.Schedule;
import org.example.domain.services.orders.commands.CreateDeliveryOrderCommand;

import javax.validation.constraints.NotNull;
import java.util.List;

public class CreateDeliveryOrderCommandDto implements CreateDeliveryOrderCommand {

    @Getter
    @NotNull
    private Long userId;

    @Getter
    @NotNull
    private Long addressId;

    @Getter
    @NotNull
    private Schedule schedule;

    @Getter
    @NotNull
    private List<Long> productIds;
}
