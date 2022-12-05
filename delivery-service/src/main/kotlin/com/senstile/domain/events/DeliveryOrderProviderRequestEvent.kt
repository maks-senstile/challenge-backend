package com.senstile.domain.events

data class DeliveryOrderProviderRequestEvent(
    override val orderId: Long,
    val address: String,
    val user: String,
    val productIds: List<Int>
): Event