package com.senstile.processor

import com.senstile.debezium.RoutingKeys
import com.senstile.domain.events.DeliveryOrderCreatedEvent
import org.springframework.stereotype.Component
import kotlin.random.Random

/**
 * Classic message router https://www.enterpriseintegrationpatterns.com/ContentBasedRouter.html
 */
@Component
class MessageAddressRouter {

    fun route(event: DeliveryOrderCreatedEvent): String {
        // emulate provider detection
        return listOf(
            RoutingKeys.DELIVERY_ORDER_ROUTED_PROVIDER_1,
            RoutingKeys.DELIVERY_ORDER_ROUTED_PROVIDER_2,
            RoutingKeys.DELIVERY_ORDER_ROUTED_PROVIDER_3
        )[Random.nextInt(3)]
    }
}