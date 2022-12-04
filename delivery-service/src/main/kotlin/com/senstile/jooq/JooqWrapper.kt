package com.senstile.jooq

import org.jooq.DSLContext

/**
 * Шьздуьуеы
 */
interface JooqWrapper {

    /**
     * Выполняет запрос и возвращает результат
     */
    suspend fun <T> request(operation: suspend (DSLContext) -> T): T

    /**
     * Выполняет запрос внутри транзакции и возвращает результат
     */
    suspend fun <T> transaction(operation: suspend (DSLContext) -> T): T
}
