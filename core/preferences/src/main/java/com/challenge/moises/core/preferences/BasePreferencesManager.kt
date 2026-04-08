package com.challenge.moises.core.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.lang.reflect.Type

/**
 * This manager provides common read and write logic, plus built-in object serialization
 * so any module can inherit it and define their own preferences.
 */
abstract class BasePreferencesManager(
    private val dataStore: DataStore<Preferences>,
    private val gson: Gson
) {

    /**
     * Reads a serialized JSON from the given string preference key.
     */
    protected fun <T> readObject(key: Preferences.Key<String>, type: Type, defaultValue: T): Flow<T> {
        return dataStore.data.map { preferences ->
            val json = preferences[key]
            if (json != null) {
                try {
                    gson.fromJson<T>(json, type) ?: defaultValue
                } catch (e: Exception) {
                    defaultValue
                }
            } else {
                defaultValue
            }
        }
    }

    /**
     * Writes a JSON string sequentially to the given preference key.
     */
    protected suspend fun <T> writeObject(key: Preferences.Key<String>, value: T) {
        dataStore.edit { preferences ->
            preferences[key] = gson.toJson(value)
        }
    }

    /**
     * Standard helper to edit datastore preferences directly.
     */
    protected suspend fun edit(transform: suspend (MutablePreferences) -> Unit) {
        dataStore.edit(transform)
    }

    /**
     * Exposes DataStore to directly transform or extract anything else.
     */
    protected val preferencesFlow: Flow<Preferences> = dataStore.data
}
