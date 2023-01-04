package com.averin.android.developer.baseui.util.delegates

import android.app.Activity
import android.os.Binder
import android.os.Bundle
import android.os.Parcelable
import androidx.core.app.BundleCompat
import androidx.fragment.app.Fragment
import kotlin.reflect.KProperty

inline fun <reified T : Any> Activity.requiredExtra(key: String, default: T) = lazy {
    val value = intent?.extras?.get(key)
    if (value is T) value else default
}

inline fun <reified T : Any> Activity.nullableExtra(key: String, default: T? = null) = lazy {
    val value = intent?.extras?.get(key)
    requireNotNull(if (value is T) value else default) { key }
}

object RequiredArgument {
    operator fun <T : Any> getValue(thisRef: Fragment, property: KProperty<*>): T {
        val args = thisRef.arguments
            ?: throw IllegalStateException(
                "Cannot read property ${property.name} " +
                    "if no arguments have been set"
            )
        @Suppress("UNCHECKED_CAST")
        return args.get(property.name) as T?
            ?: throw IllegalStateException("Property ${property.name} could not be read")
    }

    operator fun <T : Any> setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
        val args = thisRef.arguments ?: Bundle().also(thisRef::setArguments)
        val key = property.name
        when (value) {
            is String -> args.putString(key, value)
            is Boolean -> args.putBoolean(key, value)
            is ByteArray -> args.putByteArray(key, value)
            is CharArray -> args.putCharArray(key, value)
            is CharSequence -> args.putCharSequence(key, value)
            is Bundle -> args.putBundle(key, value)
            is Binder -> BundleCompat.putBinder(args, key, value)
            is Parcelable -> args.putParcelable(key, value)
            is java.io.Serializable -> args.putSerializable(key, value)
            else -> throw IllegalStateException(
                "Type ${value.javaClass.canonicalName} " +
                    "of property ${property.name} is not supported"
            )
        }
    }
}

object NullableArgument {
    operator fun <T : Any?> getValue(thisRef: Fragment, property: KProperty<*>): T? {
        @Suppress("UNCHECKED_CAST")
        return thisRef.arguments?.get(property.name) as? T
    }

    operator fun <T : Any?> setValue(thisRef: Fragment, property: KProperty<*>, value: T?) {
        if (value == null) return
        var args = thisRef.arguments
        if (args == null) {
            args = Bundle()
            thisRef.arguments = args
        }
        val key = property.name

        when (value) {
            is String -> args.putString(key, value)
            is Boolean -> args.putBoolean(key, value)
            is ByteArray -> args.putByteArray(key, value)
            is CharArray -> args.putCharArray(key, value)
            is CharSequence -> args.putCharSequence(key, value)
            is Bundle -> args.putBundle(key, value)
            is Binder -> BundleCompat.putBinder(args, key, value)
            is Parcelable -> args.putParcelable(key, value)
            is java.io.Serializable -> args.putSerializable(key, value)
            else -> throw IllegalStateException(
                "Type of property ${property.name} " +
                    "is not supported in $thisRef"
            )
        }
    }
}
