package com.senstile.jooq

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.jooq.DSLContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.coroutineContext

class DefaultJooqWrapper(
    private val dslProvider: () -> DSLContext
): JooqWrapper {

    private suspend fun getCurrentContext(): JooqContext? {
        return coroutineContext[JooqContextElement.Key]?.context
    }

    private fun <T> launchOperation(coroutineContext: CoroutineContext, operation: suspend (DSLContext) -> T, context: JooqContext): T {
        return runBlocking(coroutineContext + JooqContextElement(context)) {
            operation.invoke(context.dsl())
        }
    }

    private suspend fun <T> launchOperationAsync(coroutineContext: CoroutineContext, operation: suspend (DSLContext) -> T, context: JooqContext): T {
        return withContext(coroutineContext + JooqContextElement(context)) {
            operation.invoke(context.dsl())
        }
    }

    private suspend fun <T> withNewDSLContext(operation: suspend (DSLContext) -> T): T {
        return launchOperationAsync(coroutineContext, operation, DefaultJooqContext(dslProvider.invoke()))
    }

    override suspend fun <T> request(operation: suspend (DSLContext) -> T): T {
        return getCurrentContext()?.let {
            operation.invoke(it.dsl())
        } ?: withNewDSLContext(operation)
    }

    private suspend fun <T> transaction(context: CoroutineContext, operation: suspend (DSLContext) -> T): T {
        val dslContext: JooqContext? = getCurrentContext()

        return if (dslContext == null) {
            withNewDSLContext { c ->
                c.transactionResult { t ->
                    launchOperation(context, operation, DefaultJooqContext(t.dsl(), true))
                }
            }
        }
        else if (!dslContext.transactional()) {
            dslContext.dsl().transactionResult { t ->
                launchOperation(context, operation, DefaultJooqContext(t.dsl(), true))
            }
        }
        else {
            operation.invoke(dslContext.dsl())
        }
    }

    override suspend fun <T> transaction(operation: suspend (DSLContext) -> T): T {
        return transaction(EmptyCoroutineContext, operation)
    }
}
