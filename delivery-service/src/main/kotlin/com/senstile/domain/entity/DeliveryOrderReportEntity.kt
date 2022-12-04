package com.senstile.domain.entity

import com.senstile.user.UserAddress
import java.time.OffsetDateTime

data class DeliveryOrderReportEntity(
    val id: Long,
    val addressId: Int,
    val userId: Int,
    val productIds: List<Int>? = null,
    val createdAt: OffsetDateTime? = null,
    val status: String? = null,
    val providerName: String? = null,
    val providerOrderId: Int? = null,
    val executeAt: OffsetDateTime? = null,
    val user: UserAddress?
)