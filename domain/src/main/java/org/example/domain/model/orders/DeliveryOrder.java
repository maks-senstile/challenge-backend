package org.example.domain.model.orders;

import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.StateMachineConfig;
import lombok.*;
import org.example.domain.model.addresses.Address;
import org.example.domain.model.orders.embedded.Schedule;
import org.example.domain.model.orders.enums.OrderStatus;
import org.example.domain.model.orders.enums.OrderStatusTrigger;
import org.example.domain.model.users.User;
import org.example.domain.utils.Generator;

import java.time.Instant;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DeliveryOrder {

    public static StateMachineConfig<OrderStatus, OrderStatusTrigger> stateMachineConfig;

    static {
        stateMachineConfig = new StateMachineConfig<>();

        stateMachineConfig.configure(OrderStatus.PENDING)
                .permit(OrderStatusTrigger.START_EXECUTION, OrderStatus.PROCESSING)
                .permit(OrderStatusTrigger.ERROR_HAPPENED, OrderStatus.FAILED)
                .permit(OrderStatusTrigger.UNKNOWN_ERROR_HAPPENED, OrderStatus.INTERNAL_ERROR)
                .permit(OrderStatusTrigger.SUITABLE_PROCESSOR_NOT_FOUND, OrderStatus.INTERNAL_ERROR);

        stateMachineConfig.configure(OrderStatus.PROCESSING)
                // There was no way for orders to be completed, so I didn't add this logic too, but the way to
                // complete them appeared
                .permit(OrderStatusTrigger.COMPLETED, OrderStatus.COMPLETE)
                .permit(OrderStatusTrigger.ERROR_HAPPENED, OrderStatus.FAILED)
                .permit(OrderStatusTrigger.UNKNOWN_ERROR_HAPPENED, OrderStatus.INTERNAL_ERROR)
                .permit(OrderStatusTrigger.SUITABLE_PROCESSOR_NOT_FOUND, OrderStatus.INTERNAL_ERROR);

    }

    @EqualsAndHashCode.Include
    @Getter
    private Long id;

    @Getter
    private Address address;

    @Getter
    private Long userId;

    @Getter
    private List<Long> productIds;

    @Getter
    private Instant createdAt;

    private StateMachine<OrderStatus, OrderStatusTrigger> stateMachine;

    @Getter
    private String processor;

    @Setter
    @Getter
    private String providerOrderId;

    @Getter
    private Schedule schedule;

    public static DeliveryOrder create(Generator<Long> idGenerator, CreateDeliveryOrderConstructionOptions options) {
        return new DeliveryOrder(
                idGenerator.generate(),
                options.getAddress(),
                options.getUser().getId(),
                options.getProductIds(),
                Instant.now(),
                new StateMachine<>(OrderStatus.PENDING, DeliveryOrder.stateMachineConfig),
                null,
                null,
                options.getSchedule()
        );
    }

    public void fire(OrderStatusTrigger trigger) {
        this.stateMachine.fire(trigger);
    }

    public OrderStatus getStatus() {
        return stateMachine.getState();
    }

    public void assignTo(String processor) {
        this.processor = processor;
    }

    public interface CreateDeliveryOrderConstructionOptions {

        User getUser();

        Address getAddress();

        Schedule getSchedule();

        List<Long> getProductIds();
    }
}
