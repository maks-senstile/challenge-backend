package com.senstile.repository

import org.jooq.Record
import org.jooq.TableField

abstract class H2Repository {

    // TODO: ждем jooq 3.18 https://github.com/jOOQ/jOOQ/issues/10212
    @Suppress("UNCHECKED_CAST")
    protected fun <V, R: Record, T: TableField<R, V?>> T.asNotNull() = this as TableField<Record, V>
}