package com.senstile

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.senstile.configuration.ApplicationContextHolder
import com.senstile.domain.DeliveryOrderStatus
import com.senstile.domain.ExecutionType
import com.senstile.domain.payload.DeliveryOrderPayload
import com.senstile.support.UserAddressTestDataProducer
import com.senstile.view.DeliveryOrderView
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.jackson.JacksonConverter
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [ApplicationContextHolder::class])
@JsonTest
class DeliveryRouteTest {

    @MockkBean
    private lateinit var service: DeliveryOrderService
    @Autowired
    private lateinit var mapper: ObjectMapper

    @Test
    fun findNothing() = testApplication {
        coEvery { service.getAll() } returns emptyList()

        val response = getClient().get("/delivery/find-all-delivery-orders")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(0, response.body<List<DeliveryOrderView>>().size)
    }

    @Test
    fun findOne() = testApplication {
        coEvery { service.getAll() } returns listOf(deliveryOrderStub)

        val response = getClient().get("/delivery/find-all-delivery-orders")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(listOf(deliveryOrderStub), response.body<List<DeliveryOrderView>>())
    }

    @Test
    fun createAsapTest() = testApplication {
        val payload = DeliveryOrderPayload(
            userId = 1,
            addressId = 1,
            productIds = listOf(1, 2, 3)
        )
        coEvery { service.create(payload, any()) } returns deliveryOrderStub

        val response = getClient().post("/delivery/create-new-delivery-order") {
            contentType(ContentType.Application.Json)
            parameter("execute_at", ExecutionType.ASAP)
            setBody(payload)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(deliveryOrderStub, response.body<DeliveryOrderView>())
    }

    @Test
    fun createScheduledTest() = testApplication {
        val executeAt = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(OffsetDateTime.now())
        val payload = DeliveryOrderPayload(
            userId = 1,
            addressId = 1,
            productIds = listOf(1, 2, 3)
        )
        coEvery { service.schedule(payload, any()) } returns deliveryOrderStub

        val response = getClient().post("/delivery/create-new-delivery-order") {
            contentType(ContentType.Application.Json)
            parameter("execute_at", executeAt)
            setBody(payload)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(deliveryOrderStub, response.body<DeliveryOrderView>())
    }

    private fun ApplicationTestBuilder.getClient(): HttpClient {
        return createClient {
            install(ContentNegotiation) {
                register(ContentType.Application.Json, JacksonConverter(mapper))
            }
        }
    }

    private val deliveryOrderStub = DeliveryOrderView(
        id = 1,
        productIds = listOf(1, 2, 3),
        providerOrderId = 101,
        providerName = "name",
        user = UserAddressTestDataProducer.next(),
        status = DeliveryOrderStatus.PROCESSING.name
    )
}