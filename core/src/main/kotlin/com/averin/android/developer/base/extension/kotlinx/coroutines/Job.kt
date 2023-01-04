package com.averin.android.developer.base.extension.kotlinx.coroutines

import kotlinx.coroutines.Job

fun Job?.isNullOrCompleted() = this?.isCompleted ?: true
