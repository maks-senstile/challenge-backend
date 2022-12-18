package org.example.service.configs.properties;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Configuration
@ConfigurationProperties(
        prefix = "service"
)
@Validated
@Getter
@Setter
@EqualsAndHashCode
public class ServiceProperties {

    @Valid
    @NotNull
    private EventBusProperties eventBus;

    @Validated
    @Getter
    @Setter
    @EqualsAndHashCode
    public static class EventBusProperties {

        @Valid
        @NotNull
        private Integer executorCorePoolSize;

        @Valid
        @NotNull
        private Integer executorMaxPoolSize;

        @Valid
        @NotNull
        private Boolean executorWaitForTasksToCompleteOnShutdown;

        @Valid
        @NotNull
        private Integer executorAwaitTerminationSeconds;
    }
}
