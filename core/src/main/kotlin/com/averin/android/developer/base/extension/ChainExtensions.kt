package com.averin.android.developer.base.extension

inline fun <T> T.runIf(condition: Boolean, block: T.() -> T): T {
    return if (condition) {
        block(this)
    } else {
        this
    }
}
