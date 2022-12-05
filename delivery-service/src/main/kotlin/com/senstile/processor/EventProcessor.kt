package com.senstile.processor

import com.senstile.domain.events.Event

interface EventProcessor<T: Event> {

    fun isApplicable(routingKey: String): Boolean

    suspend fun process(event: T)
}