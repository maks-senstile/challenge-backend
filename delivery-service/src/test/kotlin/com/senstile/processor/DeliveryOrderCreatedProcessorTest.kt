package com.senstile.processor

import com.senstile.BaseTest
import com.senstile.EventSender
import com.senstile.debezium.RoutingKeys
import com.senstile.domain.events.DeliveryOrderCreatedEvent
import com.senstile.domain.events.DeliveryOrderRoutedEvent
import com.senstile.repository.DeliveryOrderRepository
import com.senstile.support.UserAddressTestDataProducer
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class DeliveryOrderCreatedProcessorTest: BaseTest() {

    private val eventSender = mockk<EventSender>(relaxed = true)
    private val deliveryOrderRepository = mockk<DeliveryOrderRepository>(relaxed = true)

    @Test
    fun test(): Unit = runBlocking {
        val messageRouter = mockk<MessageAddressRouter>().apply {
            every { route(any()) } returns RoutingKeys.DELIVERY_ORDER_ROUTED_PROVIDER_1
        }

        val processor = DeliveryOrderCreatedProcessor(
            jooq = jooqMock,
            eventSender = eventSender,
            deliveryOrderRepository = deliveryOrderRepository,
            messageRouter = messageRouter
        )

        processor.process(eventStub)

        // Check message sending
        coVerify(exactly = 1) { eventSender.send(
            DeliveryOrderRoutedEvent(
                orderId = eventStub.orderId,
                productIds = eventStub.productIds,
                user = eventStub.user,
                providerName = RoutingKeys.DELIVERY_ORDER_ROUTED_PROVIDER_1
            ),
            RoutingKeys.DELIVERY_ORDER_ROUTED_PROVIDER_1
        ) }
    }

    private val eventStub = DeliveryOrderCreatedEvent(
        orderId = 1,
        productIds = listOf(1, 2, 3),
        user = UserAddressTestDataProducer.next()
    )
}