package com.foresko.CalculatorLite.core.rest

import android.content.Context
import com.foresko.CalculatorLite.dataStore.CountryDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class MicroloansRepository @Inject constructor(
    private val apiService: ApiService,
) {
    suspend fun getStoreInfo(): Result<List<StoreInfo>> {
        return try {
            val response = apiService.infoGet()
            if (response.isSuccessful) {
                val storeInfo = response.body()
                if (storeInfo != null) {
                    Result.success(storeInfo)
                } else {
                    Result.failure(NullPointerException("Response body is null"))
                }
            } else {
                Result.failure(Exception("Failed to get store info: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

