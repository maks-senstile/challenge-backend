package org.example.service.integrations.jpa.generators;

import org.example.domain.utils.Generator;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class JdbcSequenceGenerator implements Generator<Long> {

    private static final String QUERY_TEMPLATE = "SELECT nextval('%s') FROM DUAL";

    private final JdbcTemplate jdbcTemplate;
    private String query;

    public JdbcSequenceGenerator(DataSource dataSource, String sequenceName) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.query = String.format(QUERY_TEMPLATE, sequenceName);
    }

    @Override
    public Long generate() {
        return jdbcTemplate.queryForObject(query, Long.class);
    }
}
