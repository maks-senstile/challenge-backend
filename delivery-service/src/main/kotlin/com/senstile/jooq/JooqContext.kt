package com.senstile.jooq

import org.jooq.DSLContext

interface JooqContext {
    fun dsl(): DSLContext
    fun transactional(): Boolean
}
