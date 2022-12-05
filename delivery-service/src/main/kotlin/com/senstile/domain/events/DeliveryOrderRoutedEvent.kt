package com.senstile.domain.events

import com.senstile.user.UserAddress

data class DeliveryOrderRoutedEvent(
    override val orderId: Long,
    val user: UserAddress,
    val productIds: List<Int>?,
    val providerName: String
): Event