package org.example.service.integrations.rest.controllers.orders.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.domain.model.orders.embedded.Schedule;
import org.example.domain.services.orders.commands.CreateDeliveryOrderCommand;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor
public class CreateDeliveryOrderCommandDto implements CreateDeliveryOrderCommand {

    @NotNull
    private Long userId;

    @NotNull
    private Long addressId;

    @NotNull
    @JsonProperty("executeAt")
    private Schedule schedule;

    @NotNull
    private List<Long> productIds;
}
