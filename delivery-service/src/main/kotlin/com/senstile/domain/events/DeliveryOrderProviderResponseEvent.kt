package com.senstile.domain.events

data class DeliveryOrderProviderResponseEvent(
    val orderId: Int,
    val providerOrderId: String,
    val status: Boolean
)
