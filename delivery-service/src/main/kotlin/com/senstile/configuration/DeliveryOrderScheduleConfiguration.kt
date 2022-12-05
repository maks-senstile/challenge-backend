package com.senstile.configuration

import com.senstile.DeliveryOrderService
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.runBlocking
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Scheduled
import java.util.concurrent.Executors

@Configuration
class DeliveryOrderScheduleConfiguration(
    private val deliveryOrderService: DeliveryOrderService
) {

    private val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    @Scheduled(fixedDelay = 5000)
    fun run() {
        runBlocking(dispatcher) {
            deliveryOrderService.deliveryScheduled()
        }
    }
}