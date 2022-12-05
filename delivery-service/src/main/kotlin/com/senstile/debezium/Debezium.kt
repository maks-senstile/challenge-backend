package com.senstile.debezium

import com.fasterxml.jackson.databind.ObjectMapper
import com.senstile.domain.events.Event
import com.senstile.processor.EventProcessor
import com.senstile.repository.OutboxRepository
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit
import kotlin.reflect.full.functions
import kotlin.reflect.jvm.jvmErasure

/**
 * Это наш маленький Debezium https://debezium.io/
 */
// WE DON'T TEST STUBS
@Service
class Debezium(
    private val outboxRepository: OutboxRepository,
    private val processors: List<EventProcessor<*>>,
    private val mapper: ObjectMapper
) {

    private val logger = LoggerFactory.getLogger(Debezium::class.java)

    /**
     * Настоящий Debezium смотрит лог транзакций, а мы простые
     */
    private var lastTransaction: Long = -1

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.SECONDS)
    fun loop() {
        // Какая уж тут асинхронность . . .
        runBlocking {
            // читаем из таблички и отправляем
            outboxRepository.findOne(lastTransaction)?.let {
                lastTransaction = it.id

                send(it.payload, it.type)
            }
        }
    }

    suspend fun send(event: String, routingKey: String) {
        getProcessors(routingKey).forEach { processor ->
            val eventType = processor::class.functions.first { it.name == "process" }.parameters[1].type.jvmErasure.java
            val e = mapper.readValue(event, eventType)

            logger.info("Sending event '$routingKey' into processor '${processor::class.simpleName}'.")

            processor.process(e as Event)
        }
    }


    @Suppress("UNCHECKED_CAST")
    private fun getProcessors(routingKey: String): List<EventProcessor<Event>> {
        return processors.filter { it.isApplicable(routingKey) }.mapNotNull { it as? EventProcessor<Event> }
    }
}