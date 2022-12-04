package com.senstile.configuration

import com.senstile.jooq.DefaultJooqWrapper
import com.senstile.jooq.JooqWrapper
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jooq.ConnectionProvider
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DataSourceConnectionProvider
import org.jooq.impl.DefaultConfiguration
import org.jooq.impl.DefaultDSLContext
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import javax.sql.DataSource


@Configuration
class DatabaseConfig {

    @Bean
    @ConfigurationProperties("spring.datasource")
    fun masterDatabaseConfig(): HikariConfig {
        return HikariConfig()
    }

    @Bean
    fun masterDataSource(masterDatabaseConfig: HikariConfig): DataSource {
        return HikariDataSource(masterDatabaseConfig)
    }

    @Bean
    fun masterConnectionProvider(masterDataSource: DataSource): DataSourceConnectionProvider? {
        return DataSourceConnectionProvider(TransactionAwareDataSourceProxy(masterDataSource))
    }

    @Bean
    fun configuration(connectionProvider: ConnectionProvider): DefaultConfiguration? {
        val jooqConfiguration = DefaultConfiguration()
        jooqConfiguration.set(connectionProvider)
        jooqConfiguration.set(SQLDialect.H2)
        return jooqConfiguration
    }

    @Bean
    fun dsl(connectionProvider: ConnectionProvider): DefaultDSLContext {
        return DefaultDSLContext(configuration(connectionProvider))
    }

    @Bean
    fun jooqWrapper(dsl: DSLContext): JooqWrapper {
        return DefaultJooqWrapper { dsl }
    }
}