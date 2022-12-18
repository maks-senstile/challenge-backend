package org.example.service.configs;

import org.example.domain.eventbus.EventBus;
import org.example.domain.services.orders.processors.Processor;
import org.example.domain.services.orders.processors.implementations.Provider1Processor;
import org.example.domain.services.orders.processors.implementations.Provider2Processor;
import org.example.domain.services.orders.processors.implementations.Provider3Processor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProcessorsConfig {

    @Bean("provider1Processor")
    public Processor provider1Processor(EventBus eventBus) {
        return new Provider1Processor(eventBus);
    }

    @Bean("provider2Processor")
    public Processor provider2Processor(EventBus eventBus) {
        return new Provider2Processor(eventBus);
    }

    @Bean("provider3Processor")
    public Processor provider3Processor(EventBus eventBus) {
        return new Provider3Processor(eventBus);
    }
}
