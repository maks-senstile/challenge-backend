package com.senstile.configuration

import com.senstile.ResourceLoader
import com.senstile.db.tables.references.DELIVERY_ORDER
import com.senstile.db.tables.references.DELIVERY_ORDER_REPORT
import com.senstile.db.tables.references.OUTBOX
import com.senstile.jooq.JooqWrapper
import kotlinx.coroutines.runBlocking
import org.jooq.DSLContext
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Чистим H2 БД перед каждым запуском (костыль)
 */
@Component
class DatabaseCleaner(private val dsl: DSLContext): InitializingBean {

    @Autowired
    lateinit var jooqWrapper: JooqWrapper

    override fun afterPropertiesSet() {
        runBlocking {
            jooqWrapper.request { it.execute(ResourceLoader.getAsString("db/migration/h2.sql")) }
        }
    }
}