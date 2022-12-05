package com.senstile.domain

import com.senstile.domain.events.DeliveryOrderCreatedEvent
import com.senstile.domain.events.DeliveryOrderSentEvent
import com.senstile.domain.events.Event
import kotlin.reflect.KClass

enum class EventType(val code: String, val type: KClass<out Event>) {
    DELIVERY_ORDER_CREATED("delivery.order.created", DeliveryOrderCreatedEvent::class),
    DELIVERY_ORDER_ROUTED_PROVIDER1("delivery.order.routed.provider1", DeliveryOrderCreatedEvent::class),
    DELIVERY_ORDER_ROUTED_PROVIDER2("delivery.order.routed.provider2", DeliveryOrderCreatedEvent::class),
    DELIVERY_ORDER_ROUTED_PROVIDER3("delivery.order.routed.provider3", DeliveryOrderCreatedEvent::class),
    DELIVERY_ORDER_SENT("delivery.order.sent", DeliveryOrderSentEvent::class);

    companion object {
        fun from(code: String?): EventType = values().firstOrNull { it.code == code }
            ?: throw IllegalArgumentException("Event type '$code' not supported.")
    }
}