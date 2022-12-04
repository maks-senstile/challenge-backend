package com.senstile.domain.events

import com.senstile.user.UserAddress

data class DeliveryOrderCreatedEvent(
    override val orderId: Long,
    val user: UserAddress,
    val productIds: List<Int>?
) : Event