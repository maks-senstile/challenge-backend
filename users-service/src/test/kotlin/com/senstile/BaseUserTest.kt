package com.senstile

import com.senstile.db.tables.references.ADDRESSES
import com.senstile.db.tables.references.USERS
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.ApplicationTestBuilder
import org.jooq.DSLContext
import org.junit.jupiter.api.AfterEach
import org.koin.core.context.stopKoin
import reactor.core.publisher.Mono

open class BaseUserTest {

    protected fun insertUser(dsl: DSLContext, id: Int, firstName: String) {
        Mono.from(dsl.insertInto(USERS)
            .set(USERS.ID, id)
            .set(USERS.FIRST_NAME, firstName)
        ).block()
    }

    protected fun insertAddress(
        dsl: DSLContext,
        id: Int,
        userId: Int,
        addressLine: String?,
        city: String?,
        postalCode: String?,
        country: String?
    ) {
        Mono.from(
            dsl.insertInto(ADDRESSES)
                .set(ADDRESSES.ID, id)
                .set(ADDRESSES.USER_ID, userId)
                .set(ADDRESSES.ADDRESS_LINE, addressLine)
                .set(ADDRESSES.CITY, city)
                .set(ADDRESSES.POSTAL_CODE, postalCode)
                .set(ADDRESSES.COUNTRY, country)
        ).block()
    }

    @AfterEach
    fun after() {
        stopKoin()
    }

    protected fun ApplicationTestBuilder.getClient(): HttpClient {
        return createClient {
            install(ContentNegotiation) {
                json()
            }
        }
    }

    protected fun clear(dsl: DSLContext) {
        Mono.from(dsl.deleteFrom(ADDRESSES)).block()
        Mono.from(dsl.deleteFrom(USERS)).block()
    }
}