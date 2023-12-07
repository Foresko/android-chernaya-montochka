package com.foresko.CalculatorLite.core.rest

data class ApiResponse(
    val countries: List<Country>,
    val stores: Map<String, Map<String, List<StoreInfo>>>
)

data class Country(
    val code: String,
    val flagUrl: String
)

data class StoreInfo(
    val currency: String,
    val icon: String,
    val title: String,
    val rating: Float,
    val rateFrom: Float,
    val termFrom: Int,
    val termTo: Int,
    val sumTo: Int,
    val url: String
)
