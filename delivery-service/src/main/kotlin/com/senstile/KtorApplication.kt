package com.senstile

import com.fasterxml.jackson.databind.ObjectMapper
import com.senstile.configuration.ApplicationContextHolder
import com.senstile.domain.exceptions.ObjectNotFoundException
import com.senstile.domain.payload.DeliveryOrderPayload
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.jackson.JacksonConverter
import io.ktor.server.application.*
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.requestvalidation.RequestValidation
import io.ktor.server.plugins.requestvalidation.ValidationResult
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.plugins.statuspages.exception
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing

fun Application.module() {

    install(ContentNegotiation) {
        register(
            ContentType.Application.Json,
            JacksonConverter(ApplicationContextHolder.context.getBean(ObjectMapper::class.java))
        )
    }

    install(RequestValidation) {
        validate<DeliveryOrderPayload> { customer -> customer.validate() }
    }

    install(StatusPages) {
        exception<ObjectNotFoundException> { call, cause ->
            call.respondText(text = cause.message ?: "" , status = HttpStatusCode.NotFound)
        }
        this.exception<Throwable> { call, cause ->
            call.respondText(text = cause.message ?: "" , status = HttpStatusCode.BadRequest)
        }
    }

    install(Routing) {
        queue()
    }
}
