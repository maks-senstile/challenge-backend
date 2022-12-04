package com.senstile.jooq

import org.jooq.Configuration
import org.jooq.DSLContext

class DefaultJooqContext : JooqContext {

    private val context: DSLContext
    private val transactional: Boolean

    constructor(context: DSLContext, transactional: Boolean = false) {
        this.context = context
        this.transactional = transactional
    }

    constructor(configuration: Configuration, transactional: Boolean = false) {
        this.context = configuration.dsl()
        this.transactional = transactional
    }

    constructor(context: JooqContext) {
        this.context = context.dsl()
        this.transactional = context.transactional()
    }

    override fun dsl(): DSLContext {
        return context
    }

    override fun transactional(): Boolean {
        return transactional
    }
}
