package com.foresko.CalculatorLite

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LoanCalculatorApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}