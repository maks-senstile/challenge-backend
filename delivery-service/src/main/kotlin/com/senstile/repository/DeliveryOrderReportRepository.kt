package com.senstile.repository

import com.fasterxml.jackson.databind.ObjectMapper
import com.senstile.db.tables.references.DELIVERY_ORDER_REPORT
import com.senstile.domain.DeliveryOrderStatus
import com.senstile.domain.entity.DeliveryOrderReportEntity
import com.senstile.jooq.JooqWrapper
import com.senstile.user.UserAddress
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import org.jooq.JSON
import org.jooq.impl.DSL
import org.springframework.stereotype.Repository

@Repository
class DeliveryOrderReportRepository(
    private val jooq: JooqWrapper,
    private val mapper: ObjectMapper
): H2Repository() {

    suspend fun findAll(): List<DeliveryOrderReportEntity> {
        return jooq.request { dsl ->
            dsl.select().from(DELIVERY_ORDER_REPORT)
                .asFlow().map { record ->
                    DeliveryOrderReportEntity(
                        id = record[DELIVERY_ORDER_REPORT.ID.asNotNull()],
                        userId = record[DELIVERY_ORDER_REPORT.USER_ID.asNotNull()],
                        addressId = record[DELIVERY_ORDER_REPORT.ADDRESS_ID.asNotNull()],
                        user = record[DELIVERY_ORDER_REPORT.USER_PAYLOAD]?.data()?.let {
                            mapper.readValue(it, UserAddress::class.java)
                        },
                        providerName = record[DELIVERY_ORDER_REPORT.PROVIDER_NAME],
                        providerOrderId = record[DELIVERY_ORDER_REPORT.PROVIDER_ORDER_ID],
                        productIds = record[DELIVERY_ORDER_REPORT.PRODUCT_IDS]?.toList()?.filterNotNull(),
                        status = record[DELIVERY_ORDER_REPORT.STATUS]
                    )
                }.toList()
        }
    }

    suspend fun save(
        orderId: Long,
        userId: Int,
        addressId: Int,
        user: UserAddress?,
        productIds: List<Int>?
    ) {
        jooq.request { dsl ->
            dsl.insertInto(DELIVERY_ORDER_REPORT)
                .set(DELIVERY_ORDER_REPORT.ID, orderId)
                .set(DELIVERY_ORDER_REPORT.USER_ID, userId)
                .set(DELIVERY_ORDER_REPORT.ADDRESS_ID, addressId)
                .set(DELIVERY_ORDER_REPORT.USER_PAYLOAD, user?.let {
                    JSON.valueOf(mapper.writeValueAsString(it))
                })
                .set(DELIVERY_ORDER_REPORT.PRODUCT_IDS, productIds?.toTypedArray())
                .onConflictDoNothing()
                .execute()
        }
    }

    suspend fun updateProviderName(orderId: Long, providerName: String) {
        jooq.request { dsl ->
            dsl.update(DELIVERY_ORDER_REPORT)
                .set(DELIVERY_ORDER_REPORT.PROVIDER_NAME, providerName)
                .where(DELIVERY_ORDER_REPORT.ID.eq(orderId))
                .execute()
        }
    }

    suspend fun updateStatus(orderId: Long) {
        jooq.request { dsl ->
            dsl.update(DELIVERY_ORDER_REPORT)
                .set(DELIVERY_ORDER_REPORT.STATUS, DSL
                    .`when`(DELIVERY_ORDER_REPORT.PROVIDER_ORDER_ID.isNotNull, DeliveryOrderStatus.PROCESSING.name)
                    .otherwise(DeliveryOrderStatus.PENDING.name))
                .where(DELIVERY_ORDER_REPORT.ID.eq(orderId))
                .execute()
        }
    }

    suspend fun updateProviderOrderId(orderId: Long, providerOrderId: Int) {
        jooq.request { dsl ->
            dsl.update(DELIVERY_ORDER_REPORT)
                .set(DELIVERY_ORDER_REPORT.PROVIDER_ORDER_ID, providerOrderId)
                .where(DELIVERY_ORDER_REPORT.ID.eq(orderId))
                .execute()
        }
    }
}