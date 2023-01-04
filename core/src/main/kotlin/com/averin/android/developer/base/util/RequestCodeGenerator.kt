package com.averin.android.developer.base.util

import java.util.concurrent.atomic.AtomicInteger

object RequestCodeGenerator {
    private val SEED = AtomicInteger()

    val next: Int
        get() = SEED.incrementAndGet()
}
