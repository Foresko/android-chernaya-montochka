package com.foresko.CalculatorLite.core.rest

import retrofit2.http.GET

interface ApiService {
    @GET("v1/s_microloan_calculator_first.json")
    suspend fun infoGet(): ApiResponse
}
