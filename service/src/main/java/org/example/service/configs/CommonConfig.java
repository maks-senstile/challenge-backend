package org.example.service.configs;

import org.example.domain.eventbus.EventBus;
import org.example.domain.eventbus.SimpleEventBus;
import org.example.domain.services.ExternalEventService;
import org.example.service.configs.properties.ServiceProperties;
import org.example.service.integrations.eventSystem.DoNothingExternalEventService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class CommonConfig {

    @Bean
    public ExternalEventService externalEventService() {
        return new DoNothingExternalEventService();
    }

    @Bean("eventBusExecutor")
    public ThreadPoolTaskExecutor eventBusExecutor(ServiceProperties serviceProperties) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(serviceProperties.getEventBus().getExecutorCorePoolSize());
        executor.setMaxPoolSize(serviceProperties.getEventBus().getExecutorMaxPoolSize());
        executor.setWaitForTasksToCompleteOnShutdown(serviceProperties.getEventBus().getExecutorWaitForTasksToCompleteOnShutdown());
        executor.setAwaitTerminationSeconds(serviceProperties.getEventBus().getExecutorAwaitTerminationSeconds());
        return executor;
    }

    @Bean
    public EventBus eventBus(@Qualifier("eventBusExecutor") ThreadPoolTaskExecutor executor) {
        return new SimpleEventBus(executor);
    }
}
