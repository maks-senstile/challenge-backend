package com.senstile.domain

import java.time.OffsetDateTime

/**
 * Тип выполнения.
 */
class ExecutionType(private val executeAt: String?) {

    companion object {
        const val ASAP: String = "ASAP"
    }

    fun shouldExecuteImmediately(): Boolean {
        return executeAt == null || executeAt.equals(ASAP, true)
    }

    fun getExecuteAt() = if (shouldExecuteImmediately()) null else OffsetDateTime.parse(executeAt)
}