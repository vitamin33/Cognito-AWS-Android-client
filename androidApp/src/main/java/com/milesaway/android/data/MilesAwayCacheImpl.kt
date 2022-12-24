package com.milesaway.android.data

import android.content.Context
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.milesaway.android.domain.MilesAwayCache
import com.milesaway.android.utils.getStringValue
import com.milesaway.android.utils.setValue
import timber.log.Timber
import java.io.IOException

class MilesAwayCacheImpl(
    context: Context,
) : MilesAwayCache {
    private val Context.dataStore by preferencesDataStore(
        name = PREFS_FILENAME,
        produceMigrations = { context ->
            listOf(SharedPreferencesMigration(context, PREFS_FILENAME, keysToMigrate))
        }
    )
    private val dataStore = context.dataStore

    override suspend fun saveUsername(username: String?) {
        try {
            dataStore.edit { preferences ->
                preferences.setValue(KEY_USERNAME, username)
            }
        } catch (e: IOException) {
            Timber.w(e)
        }
    }

    override suspend fun logout() {
        try {
            dataStore.edit { preferences ->
                preferences.remove(KEY_USERNAME)
            }
        } catch (ex: IOException) {
            Timber.w(ex)
        }
    }

    override suspend fun getUsername(): String? =
        dataStore.getStringValue(KEY_USERNAME)

    companion object {
        const val PREFS_FILENAME = "milesAwayCache"
        private const val USERNAME = "username"

        private val KEY_USERNAME =
            stringPreferencesKey(USERNAME)
          private val keysToMigrate = setOf(
            USERNAME,
        )
    }
}