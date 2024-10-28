package com.foresko.CalculatorLite.core.rest

data class StoreInfo(
    val currency: String,
    val icon: String,
    val title: String,
    val rating: Float,
    val rateFrom: Float,
    val rateTo: Float?,
    val sumFrom: Int?,
    val sumTo: Int,
    val termFrom: Int,
    val termTo: Int,
    val withoutPercentDays: Int?,
    val approval: String?,
    val loanFullPrice: Int?,
    val url: String
)