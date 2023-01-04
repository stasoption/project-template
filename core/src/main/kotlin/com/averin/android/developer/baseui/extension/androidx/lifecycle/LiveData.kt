package com.averin.android.developer.baseui.extension.androidx.lifecycle

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

fun <T> LiveData<T?>.observeSafe(owner: LifecycleOwner, observer: (T) -> Unit) {
    observe(owner, SafeObserver { observer(it) })
}
