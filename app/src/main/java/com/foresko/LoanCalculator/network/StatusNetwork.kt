package com.foresko.LoanCalculator.network

sealed class NetworkStatus {
    object Available : NetworkStatus()

    object Unavailable : NetworkStatus()
}