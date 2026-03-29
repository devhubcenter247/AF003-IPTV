package com.dongbv.alf_03_iptv.app

import android.util.Base64
import android.util.Log
import com.dongbv.alf_03_iptv.BuildConfig
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.tasks.await
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.lang.reflect.Type
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class RemoteConfig : KoinComponent {

    companion object {
        private const val TAG = "RemoteConfig"
    }

    private val sharedPreference: SharedPreference by inject()
    private val gson: Gson by lazy { GsonBuilder().create() }
    private val remoteConfig: FirebaseRemoteConfig

    init {
        val configSettings = remoteConfigSettings {
            fetchTimeoutInSeconds = 30L
            minimumFetchIntervalInSeconds = if (BuildConfig.build_debug) 0L else 3600L
        }
        remoteConfig = Firebase.remoteConfig.apply {
            setConfigSettingsAsync(configSettings)
        }
    }

    suspend fun fetchAndSync() {
        val activated = runCatching {
            remoteConfig.fetchAndActivate().await()
        }.getOrElse { e ->
            Log.w(TAG, "Fetch failed", e)
            false
        }
        Log.d(TAG, "fetchAndActivate: $activated")
        syncRemote(remoteConfig)
    }

    protected open fun syncRemote(remoteConfig: FirebaseRemoteConfig) {}

    inline fun <reified T> listFromCsv(
        value: String,
        defaultList: List<T> = emptyList(),
        transform: (String) -> T?
    ): List<T> {
        if (value.isBlank()) return defaultList
        return value.split(",")
            .mapNotNull { transform(it.trim()) }
            .ifEmpty { defaultList }
    }

    @Suppress("UNCHECKED_CAST")
    internal inline fun <reified T> FirebaseRemoteConfig.readAsType(
        key: String,
        default: T
    ): T {
        val rcVal = getValue(key)
        val raw = rcVal.asString().trim()

        if (rcVal.source == FirebaseRemoteConfig.VALUE_SOURCE_DEFAULT || raw.isBlank()) {
            if (T::class != Boolean::class) return default
        }

        val result: T = when (T::class) {
            Boolean::class -> rcVal.asBoolean() as T
            Int::class -> raw.toIntOrNull() as? T ?: default
            Long::class -> raw.toLongOrNull() as? T ?: default
            Float::class -> raw.toFloatOrNull() as? T ?: default
            Double::class -> raw.toDoubleOrNull() as? T ?: default
            String::class -> raw as T
            else -> readComplexType(key, raw, default)
        }

        Log.d(TAG, "readAsType key=$key, value=$result")
        return result
    }

    @Suppress("UNCHECKED_CAST")
    internal inline fun <reified T> readComplexType(
        key: String,
        raw: String,
        default: T
    ): T = when {
        T::class.java.isEnum -> {
            if (raw.isBlank()) default
            else runCatching {
                java.lang.Enum.valueOf(T::class.java as Class<out Enum<*>>, raw) as T
            }.getOrDefault(default)
        }

        T::class == ByteArray::class -> {
            if (raw.isBlank()) default
            else runCatching {
                Base64.decode(raw, Base64.DEFAULT) as T
            }.getOrDefault(default)
        }

        default is Set<*> -> {
            if (raw.isBlank()) default
            else {
                val parsed = runCatching {
                    val listType = object : TypeToken<List<String>>() {}.type
                    gson.fromJson<List<String>>(raw, listType).toSet()
                }.getOrNull() ?: raw.split(',')
                    .map { it.trim() }
                    .filter { it.isNotEmpty() }
                    .toSet()
                    .ifEmpty { default as Set<String> }
                parsed as T
            }
        }

        raw.isBlank() -> default

        else -> {
            runCatching {
                val type: Type = object : TypeToken<T>() {}.type
                gson.fromJson<T>(raw, type) ?: default
            }.getOrElse {
                Log.w(TAG, "Failed to parse key=$key", it)
                default
            }
        }
    }

    internal inline fun <reified T> FirebaseRemoteConfig.remoteValue(
        key: String,
        default: T,
    ): T {
        val source = getValue(key).source
        val value = readAsType(key, default)

        if (source != FirebaseRemoteConfig.VALUE_SOURCE_DEFAULT) {
            sharedPreference.writeAny(key, value)
        }
        return value
    }

    internal inline fun <reified T> FirebaseRemoteConfig.remoteList(
        key: String,
        default: List<T> = emptyList(),
    ): List<T> {
        val raw = getString(key).trim()
        if (raw.isBlank()) return default

        val listType: Type = object : TypeToken<List<T>>() {}.type
        val parsed = runCatching {
            gson.fromJson<List<T>>(raw, listType) ?: default
        }.getOrElse {
            Log.w(TAG, "Failed to parse list key=$key", it)
            default
        }

        val source = getValue(key).source
        if (source != FirebaseRemoteConfig.VALUE_SOURCE_DEFAULT) {
            sharedPreference.writeAny(key, gson.toJson(parsed, listType))
        }
        return parsed
    }

    internal inline fun <reified T> dataObject(
        key: String,
        default: T,
    ): ReadWriteProperty<Any?, T> {
        return object : ReadWriteProperty<Any?, T> {
            override fun getValue(thisRef: Any?, property: KProperty<*>): T {
                return sharedPreference.readAny(key, default)
            }

            override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
                sharedPreference.writeAny(key, value)
            }
        }
    }
}