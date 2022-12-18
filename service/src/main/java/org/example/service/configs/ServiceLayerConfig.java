package org.example.service.configs;

import org.example.domain.eventbus.EventBus;
import org.example.domain.model.orders.DeliveryOrderRepository;
import org.example.domain.model.users.UserRepository;
import org.example.domain.services.orders.DeliveryOrderService;
import org.example.domain.services.orders.DeliveryOrderServiceImpl;
import org.example.domain.services.orders.processors.Processor;
import org.example.domain.services.users.UserService;
import org.example.domain.services.users.UserServiceImpl;
import org.example.domain.utils.Generator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ServiceLayerConfig {
    @Bean
    public DeliveryOrderService deliveryOrderService(
            @Qualifier("ordersIdGenerator") Generator<Long> generator,
            DeliveryOrderRepository deliveryOrderRepository,
            UserRepository userRepository,
            EventBus eventBus,
            List<Processor> processors
    ) {
        return new DeliveryOrderServiceImpl(
                generator,
                deliveryOrderRepository,
                userRepository,
                eventBus,
                processors
        );
    }

    @Bean
    public UserService userService(UserRepository userRepository) {
        return new UserServiceImpl(userRepository);
    }
}
