package com.senstile.repository

import com.senstile.db.tables.references.OUTBOX
import com.senstile.domain.entity.OutboxEntity
import com.senstile.jooq.JooqWrapper
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.stereotype.Repository

@Repository
class OutboxRepository(private val jooq: JooqWrapper): H2Repository() {

    suspend fun insert(aggregateId: Long, routingKey: String, payload: String) {
        jooq.request { dsl ->
            dsl.insertInto(OUTBOX)
                .set(OUTBOX.AGGREGATE_ID, aggregateId)
                .set(OUTBOX.TYPE, routingKey)
                .set(OUTBOX.PAYLOAD, payload)
        }.awaitFirstOrNull()
    }

    suspend fun findOne(id: Long): OutboxEntity? {
        return jooq.request { dsl ->
            dsl.select().from(OUTBOX)
                .where(OUTBOX.ID.gt(id))
                .orderBy(OUTBOX.ID).limit(1)
        }.awaitFirstOrNull()?.let {
            OutboxEntity(
                id = it[OUTBOX.ID.asNotNull()],
                aggregateId = it[OUTBOX.AGGREGATE_ID],
                type = it[OUTBOX.TYPE.asNotNull()],
                payload = it[OUTBOX.PAYLOAD.asNotNull()],
                createdAt = it[OUTBOX.CREATED_AT]
            )
        }
    }
}