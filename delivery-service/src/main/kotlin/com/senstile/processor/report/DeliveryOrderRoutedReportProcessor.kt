package com.senstile.processor.report

import com.senstile.debezium.RoutingKeys
import com.senstile.domain.events.DeliveryOrderRoutedEvent
import com.senstile.processor.EventProcessor
import com.senstile.repository.DeliveryOrderReportRepository
import org.springframework.stereotype.Component

@Component
class DeliveryOrderRoutedReportProcessor(
    private val reportRepository: DeliveryOrderReportRepository
): EventProcessor<DeliveryOrderRoutedEvent> {

    override fun isApplicable(routingKey: String) = routingKey in listOf(
            RoutingKeys.DELIVERY_ORDER_ROUTED_PROVIDER_1,
            RoutingKeys.DELIVERY_ORDER_ROUTED_PROVIDER_2,
            RoutingKeys.DELIVERY_ORDER_ROUTED_PROVIDER_3
        )

    override suspend fun process(event: DeliveryOrderRoutedEvent) {
        reportRepository.updateProviderName(event.orderId, event.providerName)
        reportRepository.updateStatus(event.orderId)
    }
}