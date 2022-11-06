package com.senstile.receiveordersystem.config;

import com.senstile.receiveordersystem.converter.DeliveryOrderConverter;
import com.senstile.receiveordersystem.converter.DeliveryOrderScheduledConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ConverterConfig implements WebMvcConfigurer {

    @Bean(name = "conversionService")
    public ConversionService conversionService() {
        DefaultConversionService service = new DefaultConversionService();
        service.addConverter(new DeliveryOrderConverter());
        service.addConverter(new DeliveryOrderScheduledConverter());
        return service;
    }


}
