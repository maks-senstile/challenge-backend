package com.senstile.configuration

import io.ktor.server.servlet.ServletApplicationEngine
import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class KtorServletConfiguration {
    @Bean
    fun servletRegistrationBean(): ServletRegistrationBean<*>? {
        return ServletRegistrationBean(ServletApplicationEngine(), "/delivery/*")
    }
}
