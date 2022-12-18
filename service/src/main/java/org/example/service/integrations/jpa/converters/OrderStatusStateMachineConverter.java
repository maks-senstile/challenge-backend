package org.example.service.integrations.jpa.converters;

import com.github.oxo42.stateless4j.StateMachine;
import org.example.domain.model.orders.DeliveryOrder;
import org.example.domain.model.orders.enums.OrderStatus;
import org.example.domain.model.orders.enums.OrderStatusTrigger;

import javax.persistence.AttributeConverter;

public class OrderStatusStateMachineConverter implements AttributeConverter<StateMachine<OrderStatus, OrderStatusTrigger>, String> {

    @Override
    public String convertToDatabaseColumn(StateMachine<OrderStatus, OrderStatusTrigger> sm) {
        return sm.getState().toString();
    }

    @Override
    public StateMachine<OrderStatus, OrderStatusTrigger> convertToEntityAttribute(String s) {
        return new StateMachine<>(OrderStatus.valueOf(s), DeliveryOrder.stateMachineConfig);
    }
}
