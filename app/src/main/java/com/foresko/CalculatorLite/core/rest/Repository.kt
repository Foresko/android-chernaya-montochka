package com.foresko.CalculatorLite.core.rest

import android.content.Context
import com.foresko.CalculatorLite.dataStore.CountryDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class MicroloansRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiService: ApiService,
    private val countryDataStore: CountryDataStore
) {
    suspend fun getStoreInfo(): List<StoreInfo>? {
        val countryCode = countryDataStore.activeCountry.first()
        val response = apiService.infoGet()
        val appStoreString = when (context.appStore) {
            AppStore.GetApps -> "GetApps"
            AppStore.HuaweiAppGallery -> "HuaweiAppGallery"
            AppStore.GalaxyStore -> "GalaxyStore"
            AppStore.RuStore -> "RuStore"
            AppStore.Other -> "other"
        }

        return apiService.infoGet().stores[appStoreString]?.get(countryCode)
    }
    suspend fun getActiveCountryCode(): String {
        return countryDataStore.activeCountry.first()
    }
}
