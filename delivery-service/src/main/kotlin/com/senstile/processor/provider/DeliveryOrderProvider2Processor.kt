package com.senstile.processor.provider

import com.senstile.EventSender
import com.senstile.debezium.RoutingKeys
import com.senstile.domain.events.DeliveryOrderRoutedEvent
import com.senstile.domain.events.DeliveryOrderSentEvent
import com.senstile.jooq.JooqWrapper
import com.senstile.processor.EventProcessor
import com.senstile.repository.DeliveryOrderRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import kotlin.random.Random

/**
 * Sending order to the real partner company.
 */
@Component
class DeliveryOrderProvider2Processor(
    private val deliveryOrderRepository: DeliveryOrderRepository,
    private val jooq: JooqWrapper,
    private val eventSender: EventSender
): EventProcessor<DeliveryOrderRoutedEvent> {

    private val logger = LoggerFactory.getLogger(DeliveryOrderProvider2Processor::class.java)

    override fun isApplicable(routingKey: String) = routingKey == RoutingKeys.DELIVERY_ORDER_ROUTED_PROVIDER_2

    override suspend fun process(event: DeliveryOrderRoutedEvent) {
        logger.info("Order '${event.orderId}' has been received by delivery provider 2.")

        jooq.transaction {
            // Send message to the real provider here
            // ...
            ///
            val providerOrderId = Random.nextInt()

            deliveryOrderRepository.updateProviderOrderId(event.orderId, providerOrderId)

            eventSender.send(
                DeliveryOrderSentEvent(
                    orderId = event.orderId,
                    providerOrderId = providerOrderId
                ),
                RoutingKeys.DELIVERY_ORDER_SENT
            )
        }
    }
}