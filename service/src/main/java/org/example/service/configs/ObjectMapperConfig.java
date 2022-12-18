package org.example.service.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.domain.model.orders.embedded.Schedule;
import org.example.service.integrations.rest.converters.ScheduleDeserializer;
import org.example.service.integrations.rest.converters.ScheduleSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Schedule.class, new ScheduleDeserializer());
        module.addSerializer(Schedule.class, new ScheduleSerializer());
        mapper.registerModule(module);
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
}
