package com.foresko.CalculatorLite

import android.app.Application
import com.amplitude.api.Amplitude
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LoanCalculatorApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

        Amplitude.getInstance()
            .initialize(this, "AIzaSyBs4DmcO-Sstdicrs0AkT3UXBrmVBINJG8")
    }
}