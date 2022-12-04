package com.senstile.domain.entity

import java.time.OffsetDateTime

data class DeliveryOrderEntity(
    val id: Long,
    val addressId: Int,
    val userId: Int,
    val productIds: List<Int>? = null,
    val createdAt: OffsetDateTime? = null,
    val providerName: String? = null,
    val providerOrderId: Int? = null,
    val executeAt: OffsetDateTime? = null
)