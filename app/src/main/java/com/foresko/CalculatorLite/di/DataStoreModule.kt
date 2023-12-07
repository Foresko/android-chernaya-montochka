package com.foresko.CalculatorLite.di

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import com.foresko.CalculatorLite.dataStore.CountryDataStore
import com.foresko.CalculatorLite.dataStore.FirstStartDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Singleton
    @Provides
    fun provideFirstStartDataStore(@ApplicationContext context: Context): FirstStartDataStore {
        return FirstStartDataStore(
            preferencesDataStore = PreferenceDataStoreFactory.create(
                produceFile = { context.preferencesDataStoreFile("firstStart") }
            )
        )
    }

    @Singleton
    @Provides
    fun provideSettingsTimeDataStore(@ApplicationContext context: Context): CountryDataStore {
        return CountryDataStore(
            PreferenceDataStoreFactory.create {
                context.preferencesDataStoreFile(name = "country_data_store")
            }
        )
    }
}