package com.senstile

import com.senstile.view.AddressView
import com.senstile.view.UserView
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import org.jooq.DSLContext
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.koin.core.component.get
import org.koin.test.KoinTest

class FindOneUserTest: KoinTest, BaseUserTest() {

    @Test
    fun userNotFound() = testApplication {
        application { clear(get()) }
        val response = getClient().get("/find-user-by-id/1674")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun findUserWithoutAddressById() = testApplication {
        application {
            get<DSLContext>().let { dsl ->
                clear(dsl)
                insertUser(dsl, 1, "Иван")
            }
        }
        val response = getClient().get("/find-user-by-id/1")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(UserView(id = 1, firstName = "Иван", addresses = emptyList()), response.body<UserView>())
    }

    @Test
    fun findUserById() = testApplication {
        application {
            get<DSLContext>().let { dsl ->
                clear(dsl)
                insertUser(dsl, 1, "Петя")
                insertAddress(
                    dsl = dsl,
                    id = 1,
                    userId = 1,
                    addressLine = "Петровка 38",
                    postalCode = "111555",
                    city = "Moscow",
                    country = "Эта страна"
                )
                insertAddress(
                    dsl = dsl,
                    id = 2,
                    userId = 1,
                    addressLine = "Трубная 170",
                    postalCode = "",
                    city = "Бишкек",
                    country = "Кыргызстан"
                )
            }
        }
        val response = getClient().get("/find-user-by-id/1")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(UserView(
            id = 1,
            firstName = "Петя",
            addresses = listOf(
                AddressView(
                    id = 1,
                    addressLine = "Петровка 38",
                    postalCode = "111555",
                    city = "Moscow",
                    country = "Эта страна"
                ),
                AddressView(
                    id = 2,
                    addressLine = "Трубная 170",
                    postalCode = "",
                    city = "Бишкек",
                    country = "Кыргызстан"
                )
            )
        ), response.body<UserView>())
    }
}