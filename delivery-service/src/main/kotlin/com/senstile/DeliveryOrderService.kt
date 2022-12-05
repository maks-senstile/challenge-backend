package com.senstile

import com.senstile.debezium.RoutingKeys
import com.senstile.domain.DeliveryOrderStatus
import com.senstile.domain.ExecutionType
import com.senstile.user.UserAddress
import com.senstile.domain.events.DeliveryOrderCreatedEvent
import com.senstile.domain.entity.DeliveryOrderEntity
import com.senstile.domain.entity.DeliveryOrderReportEntity
import com.senstile.domain.payload.DeliveryOrderPayload
import com.senstile.jooq.JooqWrapper
import com.senstile.repository.DeliveryOrderReportRepository
import com.senstile.repository.DeliveryOrderRepository
import com.senstile.user.UserStore
import com.senstile.view.DeliveryOrderView
import org.springframework.stereotype.Service

@Service
class DeliveryOrderService(
    private val jooq: JooqWrapper,
    private val eventSender: EventSender,
    private val userStore: UserStore,
    private val reportRepository: DeliveryOrderReportRepository,
    private val deliveryOrderRepository: DeliveryOrderRepository
) {

    suspend fun getAll(): List<DeliveryOrderView> {
        return reportRepository.findAll().map { it.toView() }
    }

    /**
     * Создаем заказ и оповещаем заинтересованных лиц
     */
    suspend fun create(payload: DeliveryOrderPayload, executionType: ExecutionType): DeliveryOrderView {
        val userAddress = userStore.getUserAddress(payload.userId, payload.addressId)

        return jooq.transaction {
            val order = deliveryOrderRepository.save(
                userId = userAddress.id,
                addressId = userAddress.address.id,
                executeAt = executionType.getExecuteAt(),
                productIds = payload.productIds ?: emptyList()
            )

            eventSender.send(
                DeliveryOrderCreatedEvent(order.id, userAddress, payload.productIds),
                RoutingKeys.DELIVERY_ORDER_CREATED
            )

            order.toView(userAddress, DeliveryOrderStatus.PENDING)
        }
    }

    suspend fun schedule(payload: DeliveryOrderPayload, executionType: ExecutionType): DeliveryOrderView {
        val userAddress = userStore.getUserAddress(payload.userId, payload.addressId)

        return jooq.transaction {
            deliveryOrderRepository.save(
                userId = userAddress.id,
                addressId = userAddress.address.id,
                executeAt = executionType.getExecuteAt(),
                productIds = payload.productIds ?: emptyList()
            )
        }.toView(userAddress, DeliveryOrderStatus.PENDING)
    }

    suspend fun deliveryScheduled() {
        deliveryOrderRepository.findReadyToProcessOrders().forEach {
            eventSender.send(
                DeliveryOrderCreatedEvent(it.id, userStore.getUserAddress(it.userId, it.addressId), it.productIds),
                RoutingKeys.DELIVERY_ORDER_CREATED
            )
        }
    }

    fun DeliveryOrderEntity.toView(user: UserAddress, status: DeliveryOrderStatus): DeliveryOrderView {
        return DeliveryOrderView(
            id = this.id,
            productIds = this.productIds,
            providerOrderId = this.providerOrderId,
            providerName = this.providerName,
            user = user,
            status = status.name
        )
    }

    fun DeliveryOrderReportEntity.toView(): DeliveryOrderView {
        return DeliveryOrderView(
            id = this.id,
            productIds = this.productIds,
            providerOrderId = this.providerOrderId,
            providerName = this.providerName,
            user = this.user,
            status = this.status
        )
    }
}