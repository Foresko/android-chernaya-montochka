package com.makarevich.CalculatorLite.core.rest

import retrofit2.http.GET

interface MicroloansRequest {
    @GET("/v2/googleplay/microloan_calculator_lite.json")
    suspend fun infoGet(): List<Loan>
}