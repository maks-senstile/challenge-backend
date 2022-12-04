package com.senstile.domain.payload

import com.senstile.domain.ExecutionType

/**
 * Конверт для заказа, который несет дополнительную информацию
 */
data class DeliveryOrderEnvelope(
    private val deliveryOrder: DeliveryOrderPayload,
    private val executionType: ExecutionType
) {
    fun getPayload() = deliveryOrder

    fun getExecutionType() = executionType
}