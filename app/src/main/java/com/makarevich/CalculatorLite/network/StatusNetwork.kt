package com.makarevich.CalculatorLite.network

sealed class NetworkStatus {
    object Available : NetworkStatus()

    object Unavailable : NetworkStatus()
}