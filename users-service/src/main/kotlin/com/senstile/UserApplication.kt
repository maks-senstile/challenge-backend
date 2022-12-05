package com.senstile

import com.senstile.exceptions.http.ObjectNotFoundException
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respondText
import io.ktor.server.routing.routing
import io.r2dbc.spi.ConnectionFactories
import io.r2dbc.spi.ConnectionFactoryOptions
import kotlinx.serialization.json.Json
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import reactor.core.publisher.Mono


fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {

    install(Koin) {
        modules(module {
            single { UserService() }
            single { UserRepository() }
            single { configureDsl() } bind DSLContext::class
        })
    }

    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
            }
        )
    }

    install(StatusPages) {
        exception<ObjectNotFoundException> { call, cause ->
            call.respondText(text = cause.message ?: "" , status = HttpStatusCode.NotFound)
        }
    }

    routing { user() }
}

private fun configureDsl(): DSLContext {
    val connectionFactory = ConnectionFactories.get(
        ConnectionFactoryOptions
            .parse("r2dbc:h2:mem:///users")
            .mutate()
            .option(ConnectionFactoryOptions.USER, "sa")
            .option(ConnectionFactoryOptions.PASSWORD, "")
            .build()
    )

    ResourceLoader.getAsString("db/migration/h2.sql").split(";")
        .forEach { statement ->
            Mono.from(connectionFactory.create()).flatMap { Mono.from(it.createStatement(statement).execute()) }.block()
        }


    return DSL.using(connectionFactory)
}