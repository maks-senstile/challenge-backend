package com.senstile.domain.exceptions

import org.jooq.Table

class JooqInsertionReturnsNullException(table: Table<*>):
    Exception("Inserting into ${table.name} returns null.")