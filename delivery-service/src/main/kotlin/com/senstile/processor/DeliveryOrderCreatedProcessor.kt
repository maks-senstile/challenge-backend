package com.senstile.processor

import com.senstile.EventSender
import com.senstile.debezium.RoutingKeys
import com.senstile.domain.events.DeliveryOrderCreatedEvent
import com.senstile.domain.events.DeliveryOrderRoutedEvent
import com.senstile.jooq.JooqWrapper
import com.senstile.repository.DeliveryOrderRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class DeliveryOrderCreatedProcessor(
    private val jooq: JooqWrapper,
    private val eventSender: EventSender,
    private val messageRouter: MessageAddressRouter,
    private val deliveryOrderRepository: DeliveryOrderRepository
): EventProcessor<DeliveryOrderCreatedEvent> {

    override fun isApplicable(routingKey: String) = routingKey == RoutingKeys.DELIVERY_ORDER_CREATED

    private val logger = LoggerFactory.getLogger(DeliveryOrderCreatedProcessor::class.java)

    override suspend fun process(event: DeliveryOrderCreatedEvent) {
        val providerRoute = messageRouter.route(event)

        logger.info("Order '${event.orderId}' will be sent to the '$providerRoute' provider.")

        jooq.transaction {
            deliveryOrderRepository.updateProviderName(event.orderId, providerRoute)

            eventSender.send(
                DeliveryOrderRoutedEvent(
                    orderId = event.orderId,
                    productIds = event.productIds,
                    user = event.user,
                    providerName = providerRoute
                ),
                providerRoute
            )
        }
    }
}