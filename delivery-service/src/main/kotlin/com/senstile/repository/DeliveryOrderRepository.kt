package com.senstile.repository

import com.senstile.db.tables.references.DELIVERY_ORDER
import com.senstile.domain.entity.DeliveryOrderEntity
import com.senstile.domain.exceptions.JooqInsertionReturnsNullException
import com.senstile.jooq.JooqWrapper
import org.jooq.Record
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository
import java.time.OffsetDateTime

@Repository
class DeliveryOrderRepository(private val jooq: JooqWrapper): H2Repository() {

    suspend fun save(
        addressId: Int,
        userId: Int,
        executeAt: OffsetDateTime?,
        productIds: List<Int>
    ): DeliveryOrderEntity {
        return jooq.request { dsl ->
                dsl.insertInto(DELIVERY_ORDER)
                    .set(DELIVERY_ORDER.ADDRESS_ID, addressId)
                    .set(DELIVERY_ORDER.USER_ID, userId)
                    .set(DELIVERY_ORDER.EXECUTE_AT, executeAt)
                    .set(DELIVERY_ORDER.PRODUCT_IDS, productIds.toTypedArray())
                    .set(DELIVERY_ORDER.CREATED_AT, DSL.currentOffsetDateTime())
                    .returning()
            }.fetchOne()?.toDeliveryOrderEntity() ?: throw JooqInsertionReturnsNullException(DELIVERY_ORDER)
    }

    suspend fun updateProviderName(orderId: Long, providerName: String) {
        jooq.request { dsl ->
            dsl.select().from(DELIVERY_ORDER).fetch().forEach {
                println(it)
            }
        }
        jooq.request { dsl ->
            dsl.update(DELIVERY_ORDER)
                .set(DELIVERY_ORDER.PROVIDER_NAME, providerName)
                .where(DELIVERY_ORDER.ID.eq(orderId))
                .execute()
        }
    }

    suspend fun updateProviderOrderId(orderId: Long, providerOrderId: Int) {
        jooq.request { dsl ->
            dsl.update(DELIVERY_ORDER)
                .set(DELIVERY_ORDER.PROVIDER_ORDER_ID, providerOrderId)
                .where(DELIVERY_ORDER.ID.eq(orderId))
                .execute()
        }
    }

    suspend fun findReadyToProcessOrders(): List<DeliveryOrderEntity> {
        return jooq.request { dsl ->
            dsl.select().from(DELIVERY_ORDER)
                .where(DELIVERY_ORDER.PROVIDER_NAME.isNull)
                .and(DELIVERY_ORDER.EXECUTE_AT.isNotNull)
                .and(DELIVERY_ORDER.EXECUTE_AT.lt(DSL.currentOffsetDateTime()))
                .fetch().map { it.toDeliveryOrderEntity() }
        }
    }

    fun Record.toDeliveryOrderEntity(): DeliveryOrderEntity {
        return DeliveryOrderEntity(
            id = this[DELIVERY_ORDER.ID.asNotNull()],
            userId = this[DELIVERY_ORDER.USER_ID.asNotNull()],
            addressId = this[DELIVERY_ORDER.ADDRESS_ID.asNotNull()],
            productIds = this[DELIVERY_ORDER.PRODUCT_IDS]?.filterNotNull()?.toList(),
            executeAt = this[DELIVERY_ORDER.EXECUTE_AT],
            providerName = this[DELIVERY_ORDER.PROVIDER_NAME],
            providerOrderId = this[DELIVERY_ORDER.PROVIDER_ORDER_ID],
            createdAt = this[DELIVERY_ORDER.CREATED_AT]
        )
    }
}