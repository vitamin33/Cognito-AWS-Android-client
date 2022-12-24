package com.milesaway.android.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import timber.log.Timber
import java.io.IOException

fun <T> MutablePreferences.setValue(key: Preferences.Key<T>, value: T?) {
    if (value == null) {
        remove(key)
    } else {
        this[key] = value
    }
}

suspend fun <T> DataStore<Preferences>.setValue(key: Preferences.Key<T>, value: T?) {
    try {
        edit { preferences ->
            preferences.setValue(key, value)
        }
    } catch (e: IOException) {
        Timber.w(e)
    }
}

suspend fun <T> DataStore<Preferences>.safeRemove(key: Preferences.Key<T>) {
    try {
        edit { preferences ->
            preferences.remove(key)
        }
    } catch (e: IOException) {
        Timber.w(e)
    }
}

fun DataStore<Preferences>.catchIOExceptions(): Flow<Preferences> =
    data.catch { exception ->
        // dataStore.data throws an IOException when an error is encountered when reading data
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }

suspend fun DataStore<Preferences>.getSafePreferences(): Preferences =
    catchIOExceptions().first()

suspend fun DataStore<Preferences>.getStringValue(
    key: Preferences.Key<String>,
    defValue: String? = null
): String? =
    getSafePreferences()[key] ?: defValue

suspend fun DataStore<Preferences>.getBooleanValue(
    key: Preferences.Key<Boolean>,
    defValue: Boolean = false
): Boolean =
    getSafePreferences()[key] ?: defValue

suspend fun DataStore<Preferences>.getLongValue(
    key: Preferences.Key<Long>,
    defValue: Long? = null
): Long? =
    getSafePreferences()[key] ?: defValue

suspend fun DataStore<Preferences>.getSetStringValue(
    key: Preferences.Key<Set<String>>,
    defValue: Set<String>? = null
): Set<String>? =
    getSafePreferences()[key] ?: defValue