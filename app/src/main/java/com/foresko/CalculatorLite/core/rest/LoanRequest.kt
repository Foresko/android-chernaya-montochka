package com.foresko.CalculatorLite.core.rest

import retrofit2.http.GET

interface ApiService {
    @GET("v1/microloans_second.json")
    suspend fun infoGet(): ApiResponse
}
