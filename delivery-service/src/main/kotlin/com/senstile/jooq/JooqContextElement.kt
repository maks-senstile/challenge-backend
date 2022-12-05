package com.senstile.jooq

import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

data class JooqContextElement(val context: JooqContext): AbstractCoroutineContextElement(JooqContextElement) {

    companion object Key: CoroutineContext.Key<JooqContextElement>

    override fun toString(): String {
        return context.toString()
    }
}