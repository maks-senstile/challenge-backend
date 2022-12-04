package com.senstile

import com.fasterxml.jackson.databind.ObjectMapper
import com.senstile.domain.events.Event
import com.senstile.repository.OutboxRepository
import org.springframework.stereotype.Component

@Component
class EventSender(
    private val mapper: ObjectMapper,
    private val outboxRepository: OutboxRepository
) {

    suspend fun send(event: Event, routingKey: String) {
        outboxRepository.insert(event.orderId, routingKey, mapper.writeValueAsString(event))
    }
}