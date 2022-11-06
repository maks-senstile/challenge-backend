package com.senstile.receiveordersystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ExecutorServiceConfig {

    @CustomExecutorService
    @Bean(destroyMethod = "shutdown")
    public ExecutorService clientExecutorService() {
        return Executors.newCachedThreadPool();
    }

}
