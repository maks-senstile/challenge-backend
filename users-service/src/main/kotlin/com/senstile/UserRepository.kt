package com.senstile

import com.senstile.db.tables.references.ADDRESSES
import com.senstile.db.tables.references.USERS
import com.senstile.entity.AddressEntity
import com.senstile.entity.UserEntity
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.jooq.DSLContext
import org.jooq.JSON
import org.jooq.Record
import org.jooq.TableField
import org.jooq.impl.DSL
import org.jooq.impl.DSL.jsonObject
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import reactor.core.publisher.Flux

class UserRepository: KoinComponent {

    private val dsl by inject<DSLContext>()

    private val userFields = listOf(
        USERS.ID,
        USERS.FIRST_NAME,
        DSL.jsonArrayAgg(
            DSL.`when`(ADDRESSES.ID.isNotNull, jsonObject(
                DSL.jsonEntry("id", ADDRESSES.ID),
                DSL.jsonEntry("country", ADDRESSES.COUNTRY),
                DSL.jsonEntry("postalCode", ADDRESSES.POSTAL_CODE),
                DSL.jsonEntry("addressLine", ADDRESSES.ADDRESS_LINE),
                DSL.jsonEntry("city", ADDRESSES.CITY)
            )).otherwise(DSL.value(null, JSON::class.java))).`as`("addresses")
    )

    suspend fun findUserById(id: Int): UserEntity? {
        return Flux.from(dsl.select(userFields)
            .from(USERS)
            .leftJoin(ADDRESSES).on(ADDRESSES.USER_ID.eq(USERS.ID))
            .where(USERS.ID.eq(id))
            .groupBy(USERS.ID, USERS.FIRST_NAME))
            .awaitFirstOrNull()
            ?.let { mapUserEntity(it) }
    }

    private fun mapUserEntity(record: Record): UserEntity {
        return UserEntity(
            id = record[USERS.ID.asNotNull()] ,
            firstName = record[USERS.FIRST_NAME],
            addresses = record.get("addresses", JSON::class.java)?.data()?.let { r ->
                Json.decodeFromString<List<AddressEntity>>(r)
            }
        )
    }

    suspend fun findAll(limit: Int, offset: Int): List<UserEntity> {
        return Flux.from(dsl.select(userFields)
            .from(USERS)
            .leftJoin(ADDRESSES).on(ADDRESSES.USER_ID.eq(USERS.ID))
            .groupBy(USERS.ID, USERS.FIRST_NAME)
            .limit(limit).offset(offset))
            .asFlow()
            .map { mapUserEntity(it) }
            .toList()
    }

    // TODO: ждем jooq 3.18 https://github.com/jOOQ/jOOQ/issues/10212
    @Suppress("UNCHECKED_CAST")
    private fun <V, R: Record, T: TableField<R, V?>> T.asNotNull() = this as TableField<Record, V>
}