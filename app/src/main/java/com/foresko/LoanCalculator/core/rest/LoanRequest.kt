package com.foresko.LoanCalculator.core.rest

import retrofit2.http.GET

interface MicroloansRequest {
    @GET("/v2/rustore/microloan_lite.json")
    suspend fun infoGet(): List<Loan>
}