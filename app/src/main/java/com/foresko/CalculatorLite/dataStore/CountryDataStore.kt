package com.foresko.CalculatorLite.dataStore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CountryDataStore(
    private val dataStore: DataStore<Preferences>
){
    companion object {
        private val ACTIVE_COUNTRY_KEY = stringPreferencesKey ("active_country")
    }

    val activeCountry: Flow<String> = dataStore.data.map { preferences ->
        preferences[ACTIVE_COUNTRY_KEY] ?: "ru"
    }

    suspend fun setActiveCountry(newValue: String) {
        dataStore.edit { preferences ->
            preferences[ACTIVE_COUNTRY_KEY] = newValue
        }
    }
}