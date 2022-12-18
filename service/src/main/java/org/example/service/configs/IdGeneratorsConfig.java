package org.example.service.configs;

import org.example.domain.utils.Generator;
import org.example.service.integrations.jpa.generators.JdbcSequenceGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class IdGeneratorsConfig {

    private static final String ADDRESSES_SEQUENCE = "addresses_sq";
    private static final String ORDERS_SEQUENCE = "orders_sq";
    private static final String USERS_SEQUENCE = "users_sq";

    @Bean("addressesIdGenerator")
    public Generator<Long> addressesIdGenerator(DataSource dataSource) {
        return new JdbcSequenceGenerator(dataSource, ADDRESSES_SEQUENCE);
    }

    @Bean("ordersIdGenerator")
    public Generator<Long> ordersIdGenerator(DataSource dataSource) {
        return new JdbcSequenceGenerator(dataSource, ORDERS_SEQUENCE);
    }

    @Bean("usersIdGenerator")
    public Generator<Long> usersIdGenerator(DataSource dataSource) {
        return new JdbcSequenceGenerator(dataSource, USERS_SEQUENCE);
    }
}
