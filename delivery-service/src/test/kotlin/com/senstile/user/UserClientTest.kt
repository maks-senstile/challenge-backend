package com.senstile.user

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.senstile.configuration.DeliveryProperties
import com.senstile.domain.exceptions.ObjectNotFoundException
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpResponseData
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.jackson.jackson
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UserClientTest {

    @Test
    fun clientNotFound(): Unit = runBlocking {
        val client = httpClient {
            respondError(HttpStatusCode.NotFound)
        }
        val userClient = UserClient(client, DeliveryProperties().apply {
            userServiceUrl = "http://localhost:8080"
        })

        assertThrows<ObjectNotFoundException> {
            userClient.requireUser(2)
        }
    }

    private fun httpClient(block: MockRequestHandleScope.() -> HttpResponseData): HttpClient {
        return HttpClient(MockEngine { _ -> block() }) {
            install(ContentNegotiation) {
                jackson {
                    this.propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE
                }
            }
        }
    }

    @Test
    fun findOneClient() = runBlocking {
        val client = httpClient {
            respond(
                content = stubUserResponse,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val userClient = UserClient(client, DeliveryProperties().apply {
            userServiceUrl = "http://localhost:8080"
        })
        val user = userClient.requireUser(1)

        assertEquals(1, user.id)
        assertEquals("Вася", user.firstName)
    }

    private val stubUserResponse = """
        {
        	"id": 1,
        	"first_name": "Вася",
        	"addresses": [
        		{
        			"id": 1,
        			"address_line": "Ленина 10",
        			"city": "Moscow",
        			"country": "KG",
        			"postal_code": "111555"
        		}
        	]
        }
    """.trimIndent()
}