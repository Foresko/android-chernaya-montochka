package com.foresko.CalculatorLite.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FirstStartDataStore (private val preferencesDataStore: DataStore<Preferences>) :
    DataStore<Boolean> {
    companion object {
        val nameKey = booleanPreferencesKey("firstStart")
    }

    override val data: Flow<Boolean>
        get() = preferencesDataStore.data.map {
            it[nameKey] ?: true
        }

    override suspend fun updateData(transform: suspend (t: Boolean) -> Boolean): Boolean {
        return withContext(Dispatchers.IO) {
            preferencesDataStore.edit { preferences ->
                val currentValue = preferences[nameKey] ?: true
                val newValue = transform(currentValue)
                preferences[nameKey] = newValue
            }
            preferencesDataStore.data.first()[nameKey] ?: true
        }
    }
}