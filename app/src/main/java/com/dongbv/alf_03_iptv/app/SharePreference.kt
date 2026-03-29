package com.dongbv.alf_03_iptv.app

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import java.lang.reflect.Type
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class SharedPreference(context: Context) {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    }

    private val gson: Gson by lazy { GsonBuilder().create() }

    private val changeListeners = mutableSetOf<OnPreferenceChangeListener>()

    interface OnPreferenceChangeListener {
        fun onPreferenceChanged(key: String)
    }

    fun registerListener(listener: OnPreferenceChangeListener) {
        changeListeners.add(listener)
    }

    fun unRegisterListener(listener: OnPreferenceChangeListener) {
        changeListeners.remove(listener)
    }

    private fun notifyChange(key: String) {
        changeListeners.forEach { it.onPreferenceChanged(key) }
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }

    private inline fun edit(block: SharedPreferences.Editor.() -> Unit) {
        sharedPreferences.edit().apply(block).apply()
    }

    private fun getString(key: String, default: String = ""): String =
        sharedPreferences.getString(key, default)?.takeIf { it.isNotEmpty() } ?: default

    private fun putString(key: String, value: String) = edit {
        putString(key, value)
        notifyChange(key)
    }

    private fun getInt(key: String, default: Int = 0): Int =
        sharedPreferences.getInt(key, default)

    private fun putInt(key: String, value: Int) = edit {
        putInt(key, value)
        notifyChange(key)
    }

    private fun getBoolean(key: String, default: Boolean = false): Boolean =
        sharedPreferences.getBoolean(key, default)

    private fun putBoolean(key: String, value: Boolean) = edit {
        putBoolean(key, value)
        notifyChange(key)
    }

    private fun getFloat(key: String, default: Float = 0f): Float =
        sharedPreferences.getFloat(key, default)

    private fun putFloat(key: String, value: Float) = edit {
        putFloat(key, value)
        notifyChange(key)
    }

    private fun getLong(key: String, default: Long = 0L): Long =
        sharedPreferences.getLong(key, default)

    private fun putLong(key: String, value: Long) = edit {
        putLong(key, value)
        notifyChange(key)
    }

    private fun getDouble(key: String, default: Double = 0.0): Double =
        sharedPreferences.getString(key, null)?.toDoubleOrNull() ?: default

    private fun putDouble(key: String, value: Double) = edit {
        putString(key, value.toString())
        notifyChange(key)
    }

    @Suppress("UNCHECKED_CAST")
    internal fun <T> getValueFor(key: String, default: T, type: Type? = null): T = when (default) {
        is String -> getString(key, default)
        is Int -> getInt(key, default)
        is Boolean -> getBoolean(key, default)
        is Float -> getFloat(key, default)
        is Long -> getLong(key, default)
        is Double -> getDouble(key, default)
        is Set<*> -> {
            val def = default.filterIsInstance<String>().toSet()
            (sharedPreferences.getStringSet(key, def) ?: def)
        }
        is Enum<*> -> getEnumByNameOrOrdinal(key, default)
        else -> getJsonValue(key, default, type)
    } as T

    @Suppress("UNCHECKED_CAST")
    internal fun <T> setValueFor(key: String, value: T) = when (value) {
        is String -> putString(key, value)
        is Int -> putInt(key, value)
        is Boolean -> putBoolean(key, value)
        is Float -> putFloat(key, value)
        is Long -> putLong(key, value)
        is Double -> putDouble(key, value)
        is Enum<*> -> putString(key, value.name)
        is Set<*> -> {
            sharedPreferences.edit()
                .putStringSet(key, value.filterIsInstance<String>().toSet())
                .apply()
            notifyChange(key)
        }
        else -> {
            putString(key, gson.toJson(value))
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> getJsonValue(key: String, default: T, type: Type? = null): T {
        val json = sharedPreferences.getString(key, null)
        if (json.isNullOrBlank()) return default
        return try {
            val resolvedType = type ?: default!!::class.java
            gson.fromJson<T>(json, resolvedType) ?: default
        } catch (e: Exception) {
            default
        }
    }

    internal inline fun <reified T> list(
        key: String,
        default: List<T> = emptyList()
    ): ReadWriteProperty<Any?, List<T>> {
        val listType: Type = object : TypeToken<List<T>>() {}.type
        return ListPreferenceProperty(this, key, default, listType, gson)
    }

    internal class ListPreferenceProperty<T>(
        private val prefs: SharedPreference,
        private val key: String,
        private val default: List<T>,
        private val listType: Type,
        private val gson: Gson,
    ) : ReadWriteProperty<Any?, List<T>> {

        override fun getValue(thisRef: Any?, property: KProperty<*>): List<T> {
            val json = prefs.getString(key, "")
            if (json.isBlank()) return default
            return runCatching { gson.fromJson<List<T>>(json, listType) }
                .getOrDefault(default)
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: List<T>) {
            prefs.putString(key, gson.toJson(value, listType))
            prefs.notifyChange(key)
        }
    }

    fun boolean(key: String, default: Boolean = false) =
        createProperty(key, default, ::getBoolean, ::putBoolean)

    fun string(key: String, default: String = "") =
        createProperty(key, default, ::getString, ::putString)

    fun int(key: String, default: Int = 0) =
        createProperty(key, default, ::getInt, ::putInt)

    fun long(key: String, default: Long = 0L) =
        createProperty(key, default, ::getLong, ::putLong)

    fun float(key: String, default: Float = 0f) =
        createProperty(key, default, ::getFloat, ::putFloat)

    fun double(key: String, default: Double = 0.0) =
        createProperty(key, default, ::getDouble, ::putDouble)

    private fun <T> createProperty(
        key: String,
        default: T,
        getter: (String, T) -> T,
        setter: (String, T) -> Unit
    ): ReadWriteProperty<Any?, T> = object : ReadWriteProperty<Any?, T> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): T = getter(key, default)
        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) = setter(key, value)
    }

    fun <T : Any> preference(
        key: String,
        default: T
    ): ReadWriteProperty<Any, T> = object : ReadWriteProperty<Any, T> {
        override fun getValue(thisRef: Any, property: KProperty<*>): T =
            getValueFor(key, default)

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
            setValueFor(key, value)
            notifyChange(key)
        }
    }

    @Suppress("UNCHECKED_CAST")
    internal inline fun <reified T> readAny(key: String, default: T): T {
        val type: Type = object : TypeToken<T>() {}.type
        return getValueFor(key, default, type)
    }

    internal inline fun <reified T> writeAny(key: String, value: T) {
        setValueFor(key, value)
    }

    class PreferenceProperty<T : Any>(
        private val prefs: SharedPreference,
        val key: String,
        val default: T
    ) {
        var value: T by prefs.preference(key, default)
        val liveData: LiveData<T> = prefs.liveData(key, default)
        val flow: Flow<T> = prefs.flow(key, default)
    }

    fun <T : Any> preferenceOf(key: String, default: T): PreferenceProperty<T> =
        PreferenceProperty(this, key, default)

    fun <T : Any> liveData(key: String, default: T): LiveData<T> = object : LiveData<T>() {
        private val listener = OnSharedPreferenceChangeListener { _, changedKey ->
            if (changedKey == key) postValue(getValueFor(key, default))
        }

        override fun onActive() {
            super.onActive()
            value = getValueFor(key, default)
            sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        }

        override fun onInactive() {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
            super.onInactive()
        }
    }

    fun <T : Any> flow(key: String, default: T): Flow<T> = callbackFlow {
        trySend(getValueFor(key, default))
        val listener = OnSharedPreferenceChangeListener { _, changedKey ->
            if (changedKey == key) trySend(getValueFor(key, default))
        }
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        awaitClose { sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }.distinctUntilChanged()

    inline fun <reified T : Enum<T>> getEnum(key: String, default: T): T =
        migrateEnumToString(key, T::class.java, default)

    fun <T : Enum<T>> migrateEnumToString(key: String, enumClass: Class<T>, default: T): T {
        return try {
            val stringValue = sharedPreferences.getString(key, null)
            if (stringValue != null) {
                try {
                    java.lang.Enum.valueOf(enumClass, stringValue)
                } catch (e: IllegalArgumentException) {
                    migrateFromOrdinal(key, default)
                }
            } else {
                migrateFromOrdinal(key, default)
            }
        } catch (e: Exception) {
            default
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Enum<T>> migrateFromOrdinal(key: String, default: T): T {
        return try {
            val intValue = sharedPreferences.getInt(key, -1)
            if (intValue >= 0) {
                val enumConstants = (default::class.java as Class<out Enum<*>>).enumConstants
                if (enumConstants != null && intValue < enumConstants.size) {
                    val enumValue = enumConstants[intValue] as T
                    putString(key, enumValue.name)
                    enumValue
                } else default
            } else default
        } catch (e: Exception) {
            default
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun getEnumByNameOrOrdinal(key: String, default: Enum<*>): Enum<*> {
        val enumClass = default::class.java as Class<out Enum<*>>
        val constants = enumClass.enumConstants ?: return default

        val savedName = sharedPreferences.getString(key, null)
        if (savedName != null) {
            constants.firstOrNull { it.name == savedName }?.let { return it }
        }

        try {
            val oldOrdinal = sharedPreferences.getInt(key, -1)
            if (oldOrdinal in constants.indices) {
                val migrated = constants[oldOrdinal]
                putString(key, migrated.name)
                return migrated
            }
        } catch (_: ClassCastException) {}

        return default
    }
}