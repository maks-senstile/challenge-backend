package com.senstile.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.engine.java.Java
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.serialization.jackson.JacksonConverter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KtorClientConfiguration {

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Bean
    fun httpEngine() = Java

    @Bean
    fun ktorClient(): HttpClient {
        return HttpClient(httpEngine()) {
            expectSuccess = false

            engine {
                threadsCount = 2
                pipelining = true
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 10000
                socketTimeoutMillis = 10000
                connectTimeoutMillis = 5000
            }

            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }

            install(ContentNegotiation) {
                register(ContentType.Application.Json, JacksonConverter(mapper))
            }
        }
    }
}