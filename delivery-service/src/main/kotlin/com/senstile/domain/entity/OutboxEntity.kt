package com.senstile.domain.entity

import com.senstile.domain.EventType
import java.time.OffsetDateTime

data class OutboxEntity(
    val id: Long,
    val aggregateId: Long?,
    val type: String,
    val payload: String,
    val createdAt: OffsetDateTime?
)