package com.makarevich.CalculatorLite.ui.utils

import android.util.Log
import kotlinx.coroutines.delay
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.http.GET

private interface ApiService {
    @GET("/country-iso")
    suspend fun fetchCountryCode(): ResponseBody
}

class CountryCode {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://ifconfig.co/")
        .build()

    private val apiService = retrofit.create(ApiService::class.java)

    suspend fun isRussianIp(): Boolean {
        return try {
            val response = apiService.fetchCountryCode()

            val countryCode = response.string().trim().uppercase()

            countryCode == "RU"
        } catch (e: Exception) {
            Log.d("Country Code", "Failed to fetch country code: ${e.localizedMessage}")
            false
        }
    }
}