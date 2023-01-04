package com.averin.android.developer.base.util

import android.content.SharedPreferences
import androidx.core.content.edit
import com.squareup.moshi.Moshi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class FlowPreferences(
    moshi: Moshi,
    sharedPreferences: SharedPreferences,
    version: Int,
    onUpgrade: ((prefs: Preferences, oldVersion: Int, newVersion: Int) -> Unit)? = null,
    fallbackToDestructiveMigration: Boolean = false
) {
    private val prefs = Preferences(moshi, sharedPreferences)

    init {
        val oldVersion = prefs.get(VERSION_KEY, Int::class)
        if (oldVersion != null && oldVersion < version) {
            when {
                onUpgrade != null -> onUpgrade(prefs, oldVersion, version)
                fallbackToDestructiveMigration -> prefs.clear()
                else -> throw IllegalArgumentException(
                    "No migration found for preferences: $oldVersion->$version"
                )
            }
            prefs.set(VERSION_KEY, Int::class, version)
        }
    }

    operator fun <T : Any> get(key: String, type: KClass<T>) = Property(key, type)

    fun clear() {
        prefs.clear()
    }

    companion object {
        private const val VERSION_KEY = "__VERSION__"
        const val ARG_USER_EMAIL = "ARG_USER_EMAIL"
    }

    inner class Property<T : Any>(
        private val key: String,
        private val type: KClass<T>
    ) : ReadWriteProperty<Any?, T?> {

        var value: T?
            get() = prefs.get(key, type)
            set(value) {
                if (value == null) prefs.remove(key) else prefs.set(key, type, value)
            }

        override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
            return value
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
            this.value = value
        }

        fun asFlow() = prefs.keyFlow
            .filter { it == key }
            .map { prefs.get(key, type) }
            .onStart { emit(prefs.get(key, type)) }
    }

    class Preferences(
        private val moshi: Moshi,
        private val prefs: SharedPreferences
    ) {
        val keyFlow: Flow<String> = callbackFlow {
            val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key -> offer(key) }
            prefs.registerOnSharedPreferenceChangeListener(listener)
            awaitClose { prefs.unregisterOnSharedPreferenceChangeListener(listener) }
        }

        @Suppress("UNCHECKED_CAST")
        fun <T : Any> get(key: String, type: KClass<T>): T? {
            return if (!prefs.contains(key)) {
                null
            } else {
                when (type) {
                    String::class -> prefs.getString(key, null) as T
                    Float::class -> prefs.getFloat(key, Float.MIN_VALUE) as T
                    Int::class -> prefs.getInt(key, Int.MIN_VALUE) as T
                    Long::class -> prefs.getLong(key, Long.MIN_VALUE) as T
                    Boolean::class -> prefs.getBoolean(key, false) as T
                    else -> moshi.adapter(type.java).fromJson(prefs.getString(key, "")!!)
                }
            }
        }

        fun <T : Any> set(key: String, type: KClass<T>, value: T) = prefs.edit(true) {
            when (value) {
                is String -> putString(key, value)
                is Float -> putFloat(key, value)
                is Int -> putInt(key, value)
                is Long -> putLong(key, value)
                is Boolean -> putBoolean(key, value)
                else -> putString(key, moshi.adapter(type.java).toJson(value))
            }
        }

        fun remove(key: String) = prefs.edit { remove(key) }

        fun clear() = prefs.edit { clear() }
    }
}
