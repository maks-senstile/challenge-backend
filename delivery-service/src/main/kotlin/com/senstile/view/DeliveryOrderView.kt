package com.senstile.view

import com.senstile.user.UserAddress

data class DeliveryOrderView(
    val id: Long,
    val user: UserAddress?,
    val providerName: String?,
    val providerOrderId: Int?,
    val productIds: List<Int>?,
    val status: String?
)
