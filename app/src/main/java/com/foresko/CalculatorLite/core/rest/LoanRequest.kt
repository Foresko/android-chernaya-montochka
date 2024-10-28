package com.foresko.CalculatorLite.core.rest

import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("v2/s_microloan_calculator_first.json")
    suspend fun infoGet(): Response<List<StoreInfo>>
}
