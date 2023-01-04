package com.averin.android.developer.base.init

import androidx.annotation.IntDef

interface BaseAppInitializer {
    @Priority
    fun getPriority() = Priority.LOW
    fun onAppCreateInit()

    @IntDef(Priority.LOW, Priority.MEDIUM, Priority.HIGH)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Priority {
        companion object {
            const val LOW = 0
            const val MEDIUM = 1
            const val HIGH = 2
        }
    }
}
