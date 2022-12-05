package com.senstile.domain.payload

import io.ktor.server.plugins.requestvalidation.ValidationResult

data class DeliveryOrderPayload(
    val userId: Int,
    val addressId: Int,
    val productIds: List<Int>?
) {
    fun validate(): ValidationResult {
        return if (this.productIds.isNullOrEmpty())
            ValidationResult.Invalid("The productIds should not be empty!")
        else ValidationResult.Valid
    }
}
