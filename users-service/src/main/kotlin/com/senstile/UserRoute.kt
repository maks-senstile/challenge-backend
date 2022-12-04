package com.senstile

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.util.getOrFail
import org.koin.ktor.ext.inject

fun Route.user() {

    val userService by inject<UserService>()

    get("/find-user-by-id/{id}") {
        val id = call.parameters.getOrFail<Int>("id")

        call.respond(HttpStatusCode.OK, userService.findUser(id))
    }

    get("/find-all-users") {
        // Добавим простой paging, иначе скопытимся от первого же запроса
        val limit = call.request.queryParameters["limit"].let { it?.toIntOrNull() ?: 10 }
        val offset = call.request.queryParameters["offset"].let { it?.toIntOrNull() ?: 0 }

        call.respond(HttpStatusCode.OK, userService.findAll(limit, offset))
    }
}