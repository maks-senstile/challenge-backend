package com.senstile.processor.report

import com.senstile.debezium.RoutingKeys
import com.senstile.domain.events.DeliveryOrderCreatedEvent
import com.senstile.processor.EventProcessor
import com.senstile.repository.DeliveryOrderReportRepository
import org.springframework.stereotype.Component

/**
 * Save an order and send an event.
 */
@Component
class DeliveryOrderCreatedReportProcessor(
    private val reportRepository: DeliveryOrderReportRepository
): EventProcessor<DeliveryOrderCreatedEvent> {

    override fun isApplicable(routingKey: String) = routingKey == RoutingKeys.DELIVERY_ORDER_CREATED

    override suspend fun process(event: DeliveryOrderCreatedEvent) {
        // Writing an order
        reportRepository.save(
            orderId = event.orderId,
            userId = event.user.id,
            addressId = event.user.address.id,
            user = event.user,
            productIds = event.productIds
        )
        reportRepository.updateStatus(event.orderId)
    }
}