package com.senstile.repository

import com.senstile.configuration.DatabaseConfig
import com.senstile.jooq.JooqWrapper
import com.senstile.ResourceLoader
import com.zaxxer.hikari.HikariConfig
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [
    DatabaseConfig::class,
    DeliveryOrderRepository::class,
    DeliveryOrderRepositoryTest.DatabaseTestConfig::class
])
class DeliveryOrderRepositoryTest {

    @TestConfiguration
    class DatabaseTestConfig() {

        @Bean
        @Primary
        fun masterDatabaseConfig(): HikariConfig {
            return HikariConfig().apply {
                jdbcUrl = "jdbc:h2:mem:delivery"
                username = "sa"
                password = ""
            }
        }
    }

    @Autowired
    private lateinit var jooq: JooqWrapper

    @BeforeEach
    fun beforeEach(): Unit = runBlocking {
        jooq.request {
            val s = ResourceLoader.getAsString("db/migration/h2.sql")
            it.execute(s)
        }
    }

    @Autowired
    private lateinit var repository: DeliveryOrderRepository

    @Test
    fun testSave(): Unit = runBlocking {
        val entity = repository.save(1, 1, null, listOf(1, 2, 3))

        assertEquals(1, entity.addressId)
    }
}