package com.senstile.domain.events

data class DeliveryOrderSentEvent(
    override val orderId: Long,
    val providerOrderId: Int
): Event