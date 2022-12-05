package com.senstile

import com.senstile.jooq.JooqWrapper
import io.mockk.coEvery
import io.mockk.coInvoke
import io.mockk.mockk
import org.jooq.DSLContext
import org.jooq.impl.DefaultConfiguration

abstract class BaseTest {

    protected val jooqMock = mockk<JooqWrapper>().apply {
        coEvery { transaction(captureLambda<suspend (DSLContext) -> Any?>()) } answers
                { lambda<suspend (DSLContext) -> Any?>().coInvoke(DefaultConfiguration().dsl()) }
        coEvery { request (captureLambda<suspend (DSLContext) -> Any?>()) } answers
                { lambda<suspend (DSLContext) -> Any?>().coInvoke(DefaultConfiguration().dsl()) }
    }
}