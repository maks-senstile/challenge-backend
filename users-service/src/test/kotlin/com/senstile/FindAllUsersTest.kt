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

class FindAllUsersTest: KoinTest, BaseUserTest() {

    @Test
    fun findNothing() = testApplication {
        application { clear(get()) }

        val response = getClient().get("/find-all-users")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(0, response.body<List<UserView>>().size)
    }

    @Test
    fun findSeveralUsers() = testApplication {
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
                insertUser(dsl, 2, "Вася")
            }
        }
        val response = getClient().get("/find-all-users")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(listOf(
            UserView(
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
            ),
            UserView(id = 2, firstName = "Вася", addresses = emptyList())
        ), response.body<List<UserView>>())
    }
}