package com.senstile

import com.senstile.configuration.ApplicationContextHolder
import com.senstile.domain.ExecutionType
import com.senstile.domain.payload.DeliveryOrderPayload
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.queue() {

    val service by lazy {
        ApplicationContextHolder.context.getBean(DeliveryOrderService::class.java)
    }

    route("/delivery") {

        post("/create-new-delivery-order") {
            val payload = call.receive<DeliveryOrderPayload>()
            val executionType = ExecutionType(call.request.queryParameters["execute_at"])

            val order = if (executionType.shouldExecuteImmediately())
                service.create(payload, executionType)
            else
                service.schedule(payload, executionType)

            call.respond(HttpStatusCode.OK, order)
        }

        get("/find-all-delivery-orders") {
            // TODO: pagination will be added later
            call.respond(HttpStatusCode.OK, service.getAll())
        }
    }
}