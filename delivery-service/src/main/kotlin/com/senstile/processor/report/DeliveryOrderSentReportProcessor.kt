package com.senstile.processor.report

import com.senstile.debezium.RoutingKeys
import com.senstile.domain.events.DeliveryOrderSentEvent
import com.senstile.processor.EventProcessor
import com.senstile.repository.DeliveryOrderReportRepository
import org.springframework.stereotype.Component

/**
 * Записываем факт отправки в репорт таблицу.
 */
@Component
class DeliveryOrderSentReportProcessor(
    private val reportRepository: DeliveryOrderReportRepository,
): EventProcessor<DeliveryOrderSentEvent> {

    override fun isApplicable(routingKey: String) = routingKey == RoutingKeys.DELIVERY_ORDER_SENT

    override suspend fun process(event: DeliveryOrderSentEvent) {
        reportRepository.updateProviderOrderId(event.orderId, event.providerOrderId)
        reportRepository.updateStatus(event.orderId)
    }
}